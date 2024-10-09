from transformers import pipeline
from effets_indesirables import recuperer_effets_indesirables

# Initialisation du modèle NLP avec un modèle spécifié
nlp = pipeline("question-answering", model="distilbert-base-cased-distilled-squad")

def rechercher_mot_dans_fichier(fichier, mot):
    try:
        with open(fichier, 'r', encoding='utf-8') as f:
            lignes = f.readlines()
            lignes_trouvees = [ligne.strip() for ligne in lignes if mot.lower() in ligne.lower()]
        return lignes_trouvees
    except FileNotFoundError:
        print(f"Le fichier {fichier} est introuvable.")
        return []

def modifier_url(specid):
    base_url = "https://base-donnees-publique.medicaments.gouv.fr/affichageDoc.php?specid={}&typedoc=N#Ann3bEffetsIndesirables"
    return base_url.format(specid)

def interroger_utilisateur():
    return input("Indiquez le médicament dont vous voulez des renseignements : ")

def main():
    medicament_a_rechercher = interroger_utilisateur()
    fichier_liste_medicaments = 'Medicaments.txt'
    resultats = rechercher_mot_dans_fichier(fichier_liste_medicaments, medicament_a_rechercher)

    if resultats:
        print(f"Les médicaments trouvés contenant '{medicament_a_rechercher}' :")
        for i, resultat in enumerate(resultats):
            print(f"{i + 1}. {resultat}")

        choix = input("Entrez le numéro du médicament que vous souhaitez sélectionner : ")

        try:
            choix_numero = int(choix) - 1 
            if 0 <= choix_numero < len(resultats):
                ligne_choisie = resultats[choix_numero]
                print(f"Vous avez choisi : {ligne_choisie}")
                medicament_id = ligne_choisie.split()[0]  # Assure-toi que cette ligne a bien un identifiant en premier
                url_modifiee = modifier_url(medicament_id)
                print(f"L'URL modifiée est : {url_modifiee}")

                effets = recuperer_effets_indesirables(url_modifiee)

                # Utilisation du modèle NLP pour répondre à des questions sur les effets
                réponse = nlp(question=medicament_a_rechercher, context=' '.join(resultats))
                #print(f"Réponse de l'IA : {réponse['answer']}")
            else:
                print("Numéro de choix invalide.")
        except ValueError:
            print("Veuillez entrer un numéro valide.")
    else:
        print(f"Le médicament '{medicament_a_rechercher}' n'a pas été trouvé dans la liste.")

if __name__ == "__main__":
    main()
