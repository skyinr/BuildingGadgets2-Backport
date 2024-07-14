package com.direwolf20.buildinggadgets2.common.worlddata;

import com.direwolf20.buildinggadgets2.util.VecHelpers;
import com.direwolf20.buildinggadgets2.util.datatypes.StatePos;
import com.direwolf20.buildinggadgets2.util.datatypes.TagPos;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BG2Data extends SavedData {
    private static final String NAME = "buildinggadgets2";
    private final HashMap<UUID, ArrayList<StatePos>> undoList; //GadgetUUID -> UndoList StatePosData
    private final HashMap<UUID, ArrayList<StatePos>> copyPasteLookup; //GadgetUUID -> StatePosData
    private final HashMap<UUID, ArrayList<TagPos>> teMap; //GadgetUUID -> Tile Entity Data
    private final BiMap<UUID, String> redprintLookup; //A list of RedPrint names to UUIDs

    public BG2Data(HashMap<UUID, ArrayList<StatePos>> undoList, HashMap<UUID, ArrayList<StatePos>> copyPasteLookup, HashMap<UUID, ArrayList<TagPos>> teMap, BiMap<UUID, String> redprintLookup) {
        this.undoList = undoList;
        this.copyPasteLookup = copyPasteLookup;
        this.teMap = teMap;
        this.redprintLookup = redprintLookup;
    }

    public boolean addToRedprints(UUID uuid, String name) {
        if (redprintLookup.containsKey(uuid)) return false;
        if (redprintLookup.containsValue(name)) return false;
        redprintLookup.put(uuid, name);
        return true;
    }

    public boolean removeFromRedprints(String name) {
        if (redprintLookup.containsValue(name)) {
            UUID uuid = redprintLookup.inverse().get(name);
            redprintLookup.remove(uuid);
            copyPasteLookup.remove(uuid);
            teMap.remove(uuid);
            return true;
        }
        return false;
    }

    public UUID getRedprintUUIDfromName(String name) {
        if (redprintLookup.containsValue(name))
            return redprintLookup.inverse().get(name);
        return null;
    }

    public BiMap<UUID, String> getRedprintLookup() {
        return redprintLookup;
    }

    public boolean containsUndoList(UUID uuid) {
        return undoList.containsKey(uuid);
    }

    public void addToUndoList(UUID uuid, ArrayList<StatePos> list, World level) {
        undoList.put(uuid, list);
        this.setDirty();
    }

    public void removeFromUndoList(UUID uuid) {
        undoList.remove(uuid);
        this.setDirty();
    }

    public void addToCopyPaste(UUID uuid, ArrayList<StatePos> list) {
        copyPasteLookup.put(uuid, list);
        this.setDirty();
    }

    public void addToTEMap(UUID uuid, ArrayList<TagPos> list) {
        teMap.put(uuid, list);
        this.setDirty();
    }

    public ArrayList<StatePos> getCopyPasteList(UUID uuid, boolean remove) {
        ArrayList<StatePos> returnList = copyPasteLookup.get(uuid);
        if (remove) {
            returnList = copyPasteLookup.remove(uuid);
            this.setDirty();
        }
        return returnList;
    }

    public CompoundTag getCopyPasteListAsNBTMap(UUID uuid, boolean remove) {
        return statePosListToNBTMapArray(getCopyPasteList(uuid, remove));
    }

    public ArrayList<StatePos> peekUndoList(UUID uuid) {
        ArrayList<StatePos> posList = undoList.get(uuid);
        this.setDirty();
        return posList;
    }

    public ArrayList<StatePos> popUndoList(UUID uuid) {
        ArrayList<StatePos> posList = undoList.remove(uuid);
        this.setDirty();
        return posList;
    }

    public ArrayList<TagPos> peekTEMap(UUID uuid) {
        ArrayList<TagPos> tagList = teMap.get(uuid);
        return tagList;
    }

    public ArrayList<TagPos> getTEMap(UUID uuid) {
        ArrayList<TagPos> tagList = teMap.remove(uuid);
        this.setDirty();
        return tagList;
    }

    public static CompoundTag statePosListToNBTMapArray(ArrayList<StatePos> list) {
        CompoundTag tag = new CompoundTag();
        if (list == null || list.isEmpty()) return tag;
        ArrayList<BlockState> blockStateMap = StatePos.getBlockStateMap(list);
        ListTag blockStateMapList = StatePos.getBlockStateNBT(blockStateMap);
        int[] blocklist = new int[list.size()];
        final int[] counter = {0};
        BlockPos start = list.get(0).pos;
        BlockPos end = list.get(list.size() - 1).pos;
        AABB aabb = VecHelpers.aabbFromBlockPos(start, end);

        Map<BlockPos, BlockState> blockStateByPos = list.stream()
                .collect(Collectors.toMap(e -> e.pos, e -> e.state));

        BlockPos.betweenClosedStream(aabb).map(BlockPos::immutable).forEach(pos -> {
            BlockState blockState = blockStateByPos.get(pos);
            blocklist[counter[0]++] = blockStateMap.indexOf(blockState);
        });
        /*for (StatePos statePos : list) {
            blocklist[k] = blockStateMap.indexOf(statePos.state);
            k++;
        }*/
        tag.put("startpos", writeBlockPos(start));
        tag.put("endpos", writeBlockPos(end));
        tag.put("blockstatemap", blockStateMapList);
        tag.putIntArray("statelist", blocklist); //Todo - Short Array?
        return tag;
    }

    /**
     * Something Changed in NBTUtils.ReadBlockPos that broke templates, so i made the below as a workaround
     */
    public static BlockPos readBlockPos(CompoundTag compoundTag, String pKey) {
        if (!compoundTag.contains(pKey)) return BlockPos.ZERO;
        CompoundTag tag = compoundTag.getCompound(pKey);
        return new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
    }

    public static CompoundTag writeBlockPos(BlockPos pPos) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putInt("X", pPos.getX());
        compoundtag.putInt("Y", pPos.getY());
        compoundtag.putInt("Z", pPos.getZ());
        return compoundtag;
    }

    public static ArrayList<StatePos> statePosListFromNBTMapArray(CompoundTag tag) {
        ArrayList<StatePos> statePosList = new ArrayList<>();
        if (!tag.contains("blockstatemap") || !tag.contains("statelist")) return statePosList;
        ArrayList<BlockState> blockStateMap = StatePos.getBlockStateMapFromNBT(tag.getList("blockstatemap", Tag.TAG_COMPOUND));
        BlockPos start = readBlockPos(tag, "startpos");
        BlockPos end = readBlockPos(tag, "endpos");
        AABB aabb = VecHelpers.aabbFromBlockPos(start, end);
        int[] blocklist = tag.getIntArray("statelist");
        final int[] counter = {0};
        BlockPos.betweenClosedStream(aabb).map(BlockPos::immutable).forEach(pos -> {
            int blockStateLookup = blocklist[counter[0]++];
            BlockState blockState = blockStateMap.get(blockStateLookup);
            statePosList.add(new StatePos(blockState, pos));
        });
        return statePosList;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
        ListTag undoTagList = new ListTag();
        for (Map.Entry<UUID, ArrayList<StatePos>> entry : undoList.entrySet()) {
            CompoundTag tempTag = new CompoundTag();
            tempTag.putUUID("uuid", entry.getKey());
            ListTag tempList = new ListTag();
            for (StatePos statePos : entry.getValue()) {
                tempList.add(statePos.getTag());
            }
            tempTag.put("stateposlist", tempList);
            undoTagList.add(tempTag);
        }
        nbt.put("undolist", undoTagList);

        ListTag copyPasteTag = new ListTag();
        for (Map.Entry<UUID, ArrayList<StatePos>> entry : copyPasteLookup.entrySet()) {
            CompoundTag tempTag = new CompoundTag();
            tempTag.putUUID("uuid", entry.getKey());
            tempTag.put("stateposlist", statePosListToNBTMapArray(entry.getValue()));
            copyPasteTag.add(tempTag);
        }
        nbt.put("copypaste", copyPasteTag);

        ListTag teMapTag = new ListTag();
        for (Map.Entry<UUID, ArrayList<TagPos>> entry : teMap.entrySet()) {
            CompoundTag tempTag = new CompoundTag();
            tempTag.putUUID("uuid", entry.getKey());
            ListTag tempList = new ListTag();
            for (TagPos tagPos : entry.getValue()) {
                tempList.add(tagPos.getTag());
            }
            tempTag.put("temaplist", tempList);
            teMapTag.add(tempTag);
        }
        nbt.put("temaptag", teMapTag);

        ListTag redprintTag = new ListTag();
        for (Map.Entry<UUID, String> entry : redprintLookup.entrySet()) {
            CompoundTag tempTag = new CompoundTag();
            tempTag.putUUID("uuid", entry.getKey());
            tempTag.putString("name", entry.getValue());
            redprintTag.add(tempTag);
        }
        nbt.put("redprinttag", redprintTag);
        return nbt;
    }

    public static BG2Data readNbt(CompoundTag nbt, HolderLookup.Provider provider) {
        HashMap<UUID, ArrayList<StatePos>> undoList = new HashMap<>();
        ListTag undoTagList = nbt.getList("undolist", Tag.TAG_COMPOUND);
        for (int i = 0; i < undoTagList.size(); i++) {
            UUID uuid = undoTagList.getCompound(i).getUUID("uuid");
            ListTag statePosList = undoTagList.getCompound(i).getList("stateposlist", CompoundTag.TAG_COMPOUND);
            ArrayList<StatePos> tempList = new ArrayList<>();
            for (int j = 0; j < statePosList.size(); j++) {
                tempList.add(new StatePos(statePosList.getCompound(j)));
            }
            undoList.put(uuid, tempList);
        }

        HashMap<UUID, ArrayList<StatePos>> copyPaste = new HashMap<>();
        ListTag copyPasteList = nbt.getList("copypaste", Tag.TAG_COMPOUND);
        for (int i = 0; i < copyPasteList.size(); i++) {
            UUID uuid = copyPasteList.getCompound(i).getUUID("uuid");
            CompoundTag statePosList = copyPasteList.getCompound(i).getCompound("stateposlist");
            copyPaste.put(uuid, statePosListFromNBTMapArray(statePosList));
        }

        HashMap<UUID, ArrayList<TagPos>> teMap = new HashMap<>();
        ListTag teMapListTag = nbt.getList("temaptag", Tag.TAG_COMPOUND);
        for (int i = 0; i < teMapListTag.size(); i++) {
            UUID uuid = teMapListTag.getCompound(i).getUUID("uuid");
            ListTag temaplistTag = teMapListTag.getCompound(i).getList("temaplist", Tag.TAG_COMPOUND);
            ArrayList<TagPos> tagPosList = new ArrayList<>();
            for (int j = 0; j < temaplistTag.size(); j++) {
                TagPos tagPos = new TagPos(temaplistTag.getCompound(j));
                tagPosList.add(tagPos);
            }
            teMap.put(uuid, tagPosList);
        }

        BiMap<UUID, String> redPrints = HashBiMap.create();
        ListTag redPrintsTag = nbt.getList("redprinttag", Tag.TAG_COMPOUND);
        for (int i = 0; i < redPrintsTag.size(); i++) {
            UUID uuid = redPrintsTag.getCompound(i).getUUID("uuid");
            String name = redPrintsTag.getCompound(i).getString("name");
            redPrints.put(uuid, name);
        }
        return new BG2Data(undoList, copyPaste, teMap, redPrints);
    }

    public static BG2Data get(WorldServer world) {
        BG2Data bg2Data = world.getDataStorage().computeIfAbsent(
                new SavedData.Factory<BG2Data>(
                        () -> new BG2Data(new HashMap<>(), new HashMap<>(), new HashMap<>(), HashBiMap.create()),
                        BG2Data::readNbt
                ),
                NAME
        );

        bg2Data.setDirty();
        return bg2Data;
    }
}
