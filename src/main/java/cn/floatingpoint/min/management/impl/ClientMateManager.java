package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;

import java.util.HashSet;
import java.util.UUID;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:26:03
 */
public class ClientMateManager implements Manager {
    public HashSet<UUID> uuids;

    @Override
    public String getName() {
        return "Client Mate Manager";
    }

    @Override
    public void init() {
        uuids = new HashSet<>();
    }
}
