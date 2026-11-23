package fr.umontpellier.hai704i.hotel.api.vue;

import java.util.List;
import java.util.Map;

/**
 * L'enveloppe d'une collection.
 * @param elements les éléments de la collection
 * @param total    le nombre d'éléments trouvés
 * @param links    les liens utiles ; pour l'instant {@code self}
 */
public record PageVue<T>(List<T> elements, long total, Map<String, String> links) {
}
