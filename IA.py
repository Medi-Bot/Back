from transformers import pipeline
from effets_indesirables import recuperer_effets_indesirables

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

def interroger_utilisateur_medicament():
    return input("Indiquez le médicament dont vous voulez des renseignements : ")

def interroger_utilisateur_symptome():
    return input("Indiquez le symptôme que vous souhaitez évaluer : ")

def generer_phrase(symptome):
    phrase = (
        f"Je cherche des conseils médicaux pour une personne qui souffre de '{symptome}'. "
        f"Quels traitements ou recommandations recommanderiez-vous ?"
    )
    return phrase

def afficher_menu():
    print("Menu principal:")
    print("1. Chercher un médicament par nom")
    print("2. Chercher un médicament par symptôme")
    print("3. Obtenir une phrase pour évaluer un symptôme")
    print("4. Chercher un ou plusieurs médicaments par symptôme")
    print("5. Quitter")

def rechercher_medicament_par_nom_et_symptome(medicament, symptome, fichier_effets_secondaires):
    try:
        with open(fichier_effets_secondaires, 'r', encoding='utf-8') as f:
            lignes = f.readlines()
            for i in range(len(lignes)):
                if medicament.lower() in lignes[i].lower():
                    # Vérifiez si la ligne suivante existe avant d'y accéder
                    if i + 1 < len(lignes):
                        effets_line = lignes[i + 1]
                        # Extraction des effets secondaires
                        effets_dict = eval(effets_line.split("Effets secondaires :")[1].strip())
                        
                        # Vérification dans chaque catégorie d'effets
                        for frequence, effets in effets_dict.items():
                            for effet in effets:
                                if symptome.lower() in effet.lower():
                                    return f"{medicament} peut causer '{symptome}' ({frequence})."
                    else:
                        return f"Aucun effet secondaire connu pour {medicament}."
    except FileNotFoundError:
        print(f"Le fichier {fichier_effets_secondaires} est introuvable.")
    except Exception as e:
        print(f"Erreur lors de la lecture du fichier : {e}")
    
    return f"Aucun effet secondaire connu pour {medicament} ne cause '{symptome}'."


def rechercher_medicaments_et_symptomes(liste_medicaments, symptome, fichier_effets_secondaires):
    resultats = []
    for medicament in liste_medicaments:
        resultat = rechercher_medicament_par_nom_et_symptome(medicament, symptome, fichier_effets_secondaires)
        resultats.append(resultat)
    return resultats

def main():
    fichier_liste_medicaments = 'Medicaments.txt'
    fichier_effets_secondaires = 'EffetsSecondaires.txt'

    while True:
        afficher_menu()
        choix = input("Choisissez une option (1, 2, 3, 4 ou 5) : ").strip()

        if choix == '1':
            medicament_a_rechercher = interroger_utilisateur_medicament()
            resultats = rechercher_mot_dans_fichier(fichier_liste_medicaments, medicament_a_rechercher)

            if resultats:
                print(f"Les médicaments trouvés contenant '{medicament_a_rechercher}' :")
                for i, resultat in enumerate(resultats):
                    print(f"{i + 1}. {resultat}")
                choix_medicament = input("Entrez le numéro du médicament que vous souhaitez sélectionner : ")
                try:
                    choix_numero = int(choix_medicament) - 1
                    if 0 <= choix_numero < len(resultats):
                        ligne_choisie = resultats[choix_numero]
                        print(f"Vous avez choisi : {ligne_choisie}")
                        medicament_id = ligne_choisie.split()[0]
                        url_modifiee = modifier_url(medicament_id)
                        print(f"L'URL modifiée est : {url_modifiee}")
                        effets_indis = recuperer_effets_indesirables(url_modifiee)
                        print(f"Effets indésirables : {effets_indis}")
                    else:
                        print("Numéro de choix invalide.")
                except ValueError:
                    print("Veuillez entrer un numéro valide.")
            else:
                print(f"Le médicament '{medicament_a_rechercher}' n'a pas été trouvé dans la liste.")

        elif choix == '2':
            symptome = input("Indiquez le symptôme que vous ressentez : ")
            resultats_symptomes = rechercher_medicaments_et_symptomes([symptome], symptome, fichier_effets_secondaires)

            if resultats_symptomes:
                print(f"Médicaments potentiels pour le symptôme '{symptome}':")
                for i, resultat in enumerate(resultats_symptomes):
                    print(f"{i + 1}. {resultat}")
            else:
                print(f"Aucun médicament trouvé pour le symptôme '{symptome}'.")

        elif choix == '3':
            symptome_a_evaluer = input("Indiquez le symptôme que vous ressentez : ")
            phrase = generer_phrase(symptome_a_evaluer)
            print(f"Voici la phrase à copier : {phrase}")

        elif choix == '4':
            liste_medicaments_input = input("Indiquez les médicaments séparés par une virgule : ")
            liste_medicaments = [med.strip() for med in liste_medicaments_input.split(',')]
            symptome = interroger_utilisateur_symptome()
            resultats = rechercher_medicaments_et_symptomes(liste_medicaments, symptome, fichier_effets_secondaires)

            print(f"Résultats pour le symptôme '{symptome}' :")
            for resultat in resultats:
                print(resultat)

        elif choix == '5':
            print("Au revoir!")
            break

        else:
            print("Option invalide, veuillez choisir 1, 2, 3, 4 ou 5.")

if __name__ == "__main__":
    main()

