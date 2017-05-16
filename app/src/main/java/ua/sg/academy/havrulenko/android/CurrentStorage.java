package ua.sg.academy.havrulenko.android;

import ua.sg.academy.havrulenko.android.dao.InMemoryStorageSingleton;
import ua.sg.academy.havrulenko.android.dao.UsersDaoInterface;

/**
 * Класс с выбором способа хранения информации
 */
public class CurrentStorage {
    private static final Realisations currentRealisation = Realisations.IN_MEMORY;

    public static UsersDaoInterface getCurrent() {
        switch (currentRealisation) {
            case IN_MEMORY:
                return InMemoryStorageSingleton.getInstance();
            default:
                return InMemoryStorageSingleton.getInstance();
        }
    }

    private enum Realisations {IN_MEMORY}
}
