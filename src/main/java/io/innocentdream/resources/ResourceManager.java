package io.innocentdream.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.JsonHelper;
import io.innocentdream.utils.OS;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public final class ResourceManager {

    private ResourceManager() { throw new UnsupportedOperationException(); }

    private static final HashMap<String, ResourcePack> LOADED_PACKS = new HashMap<>();
    private static final HashMap<String, ResourcePack> UNLOADED_PACKS = new HashMap<>();
    private static final Stack<String> PACK_ORDER = new Stack<>();
    public static final Resource MISSING_TEXTURE = BeforeLoadedProvider.ASSETS.get(new Identifier("textures/missing.png"));

    public static void init() {
        File enabled = new File(OS.PATH, "enabledPacks.id");
        List<String> data;
        if (!enabled.exists()) {
            data = List.of("innocentdream");
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(enabled));
                data = new ArrayList<>();
                String s;
                while ((s = reader.readLine()) != null) {
                    data.add(s);
                }
            } catch (IOException e) {
                data = List.of("innocentdream");
            }
        }
        for (String s : data) {
            if ("innocentdream".equals(s)) {
                PACK_ORDER.add("innocentdream");
                continue;
            }
        }
    }

    public static void reloadAllPacks() {
        for (ResourcePack pack : LOADED_PACKS.values()) {
            pack.reload();
        }
    }

    private static ResourcePack getFirstPackWithResource(Identifier id) {
        for (int i = PACK_ORDER.size() - 1; i >= 0; i--) {
            ResourcePack pack = LOADED_PACKS.get(PACK_ORDER.get(i));
            if (pack.hasResource(id)) {
                return pack;
            }
        }
        return null;
    }

    public static Resource getResource(Identifier id) {
        ResourcePack pack = getFirstPackWithResource(id);
        if (pack == null) {
            return MISSING_TEXTURE;
        }
        return pack.get(id);
    }

    public static Resource getAsset(Identifier id) {
        ResourcePack pack = getFirstPackWithResource(id);
        if (pack == null) {
            return MISSING_TEXTURE;
        }
        return pack.getAsset(id);
    }

    public static Resource getData(Identifier id) {
        ResourcePack pack = getFirstPackWithResource(id);
        if (pack == null) {
            return MISSING_TEXTURE;
        }
        return pack.getData(id);
    }

    public static void enableResourcePack(String pack) {
        if (LOADED_PACKS.containsKey(pack)) {
            if (!PACK_ORDER.contains(pack)) {
                PACK_ORDER.add(pack);
            }
            return;
        }
        if (UNLOADED_PACKS.containsKey(pack)) {
            ResourcePack rsp = UNLOADED_PACKS.remove(pack);
            LOADED_PACKS.put(pack, rsp);
            PACK_ORDER.add(pack);
        }
    }

    public static void swapPackPositions(String pack1, String pack2) {
        int i = PACK_ORDER.indexOf(pack1),
                j = PACK_ORDER.indexOf(pack2);
        if (i < 0 || j < 0) {
            return;
        }
        PACK_ORDER.set(i, pack2);
        PACK_ORDER.set(j, pack1);
    }

    public static void disableResourcePack(String pack) {
        if (UNLOADED_PACKS.containsKey(pack)) {
            PACK_ORDER.remove(pack);
            return;
        }
        if (LOADED_PACKS.containsKey(pack)) {
            ResourcePack rsp = LOADED_PACKS.remove(pack);
            PACK_ORDER.remove(pack);
            UNLOADED_PACKS.put(pack, rsp);
        }
    }

    static {
        LOADED_PACKS.put("innocentdream", DefaultResourcePack.INSTANCE);
    }

}
