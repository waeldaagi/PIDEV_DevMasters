package services;

import java.util.HashMap;
import java.util.Map;

public class GeminiService {
    private final Map<String, String> responses;

    public GeminiService() {
        responses = new HashMap<>();
        initializeResponses();
    }

    private void initializeResponses() {
        // Réponses pour la gestion de projet
        responses.put("projet", 
            "Pour gérer efficacement un projet, voici les points clés :\n" +
            "1. Définir des objectifs clairs et mesurables\n" +
            "2. Planifier les étapes et les délais réalistes\n" +
            "3. Assigner les responsabilités à l'équipe\n" +
            "4. Suivre régulièrement l'avancement\n" +
            "5. Communiquer efficacement avec toutes les parties prenantes");

        // Réponses pour la gestion d'équipe
        responses.put("équipe",
            "Les meilleures pratiques pour la gestion d'équipe incluent :\n" +
            "1. Communication claire et régulière\n" +
            "2. Définition précise des rôles et responsabilités\n" +
            "3. Feedback constructif et régulier\n" +
            "4. Reconnaissance du travail accompli\n" +
            "5. Développement continu des compétences");

        // Réponses pour les priorités
        responses.put("priorité",
            "Pour définir les priorités dans un projet :\n" +
            "1. Évaluer l'urgence et l'importance de chaque tâche\n" +
            "2. Considérer l'impact sur les objectifs du projet\n" +
            "3. Tenir compte des dépendances entre les tâches\n" +
            "4. Consulter les parties prenantes clés\n" +
            "5. Utiliser des méthodes comme MoSCoW ou la matrice d'Eisenhower");

        // Réponses pour la planification
        responses.put("planification",
            "Conseils pour une bonne planification de projet :\n" +
            "1. Définir un calendrier réaliste\n" +
            "2. Identifier les jalons importants\n" +
            "3. Prévoir des marges pour les imprévus\n" +
            "4. Allouer les ressources efficacement\n" +
            "5. Mettre en place des points de contrôle réguliers");

        // Réponses pour la gestion des risques
        responses.put("risque",
            "Pour gérer les risques dans un projet :\n" +
            "1. Identifier les risques potentiels\n" +
            "2. Évaluer leur probabilité et impact\n" +
            "3. Préparer des plans de contingence\n" +
            "4. Surveiller les signaux d'alerte\n" +
            "5. Mettre à jour régulièrement l'analyse des risques");

        // Réponse par défaut
        responses.put("default",
            "Je peux vous aider sur plusieurs aspects de la gestion de projet :\n" +
            "1. Gestion d'équipe\n" +
            "2. Planification\n" +
            "3. Priorisation des tâches\n" +
            "4. Gestion des risques\n" +
            "5. Communication\n\n" +
            "Que souhaitez-vous savoir en particulier ?");
    }

    public String getResponse(String prompt) {
        prompt = prompt.toLowerCase();
        
        // Chercher la réponse la plus appropriée
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            if (prompt.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // Si aucune correspondance n'est trouvée, retourner la réponse par défaut
        if (prompt.contains("bonjour") || prompt.contains("salut")) {
            return "Bonjour ! Je suis votre assistant en gestion de projet. Comment puis-je vous aider ?";
        }
        
        return responses.get("default");
    }
} 