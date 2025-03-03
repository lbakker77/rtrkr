package de.lbakker77.retracker.main.domain;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RetrackerListCreator {

    public RetrackerList createRetrackerList(String name, UUID ownerId, String icon) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        if (icon == null || icon.isEmpty()) {
            throw new IllegalArgumentException("Icon must not be null or empty");
        }

        var newList = new RetrackerList();
        newList.setName(name);
        newList.setDefaultList(false);
        newList.setShared(false);
        newList.setOwnerId(ownerId);
        newList.setIcon(icon);
        return newList;
    }

    public RetrackerList createDefaultList(UUID ownerId) {
        var newList = new RetrackerList();
        newList.setName("default");
        newList.setDefaultList(true);
        newList.setShared(false);
        newList.setOwnerId(ownerId);
        newList.setIcon("schedule");
        return newList;
    }
}
