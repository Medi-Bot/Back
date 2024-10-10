import argparse
from transformers import pipeline
from effets_indesirables import recuperer_effets_indesirables
from rechercheEffet import rechercher_medicament_par_symptome2

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

def generer_phrase(symptome):
    phrase = (
        f"Je cherche des conseils médicaux pour une personne qui souffre de '{symptome}'. "
        f"Quels traitements ou recommandations recommanderiez-vous ?"
    )
    return phrase

def rechercher_medicament_par_nom_et_symptome(medicament, symptome, fichier_effets_secondaires):
    try:
        with open(fichier_effets_secondaires, 'r', encoding='utf-8') as f:
            lignes = f.readlines()
            for i in range(len(lignes)):
                if medicament.lower() in lignes[i].lower():
                    if i + 1 < len(lignes):
                        effets_line = lignes[i + 1]
                        try:
                            effets_dict = eval(effets_line.split("Effets secondaires :")[1].strip())
                        except (IndexError, SyntaxError):
                            print(f"Erreur lors de l'extraction des effets secondaires pour {medicament}.")
                            continue
                        
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
    parser = argparse.ArgumentParser(description="Recherche de médicaments par nom ou symptôme.")
    parser.add_argument('choix', type=int, help='Choix de l\'option (1, 2, 3, 4 ou 5)')
    parser.add_argument('parametre', help='Médicament ou symptôme à rechercher')
    parser.add_argument('--choix_medicament', type=int, help='Numéro du médicament à sélectionner')
    parser.add_argument('--symptome', help='Symptôme à évaluer pour le choix 4')

    args = parser.parse_args()

    fichier_liste_medicaments = 'Medicaments.txt'
    fichier_effets_secondaires = 'EffetsSecondaires.txt'

    if args.choix == 1 or args.choix == 5:  # Le choix 5 fait la même chose que le choix 1 mais on choisi le médicament dans la liste donner par le choix 1
        medicament_a_rechercher = args.parametre
        resultats = rechercher_mot_dans_fichier(fichier_liste_medicaments, medicament_a_rechercher)

        if resultats:
            print(f"Les médicaments trouvés contenant '{medicament_a_rechercher}' :")
            for i, resultat in enumerate(resultats):
                print(f"{i + 1}. {resultat}")
            
            if args.choix_medicament is not None:
                choix_medicament = args.choix_medicament - 1
                if 0 <= choix_medicament < len(resultats):
                    ligne_choisie = resultats[choix_medicament]
                    print(f"Vous avez choisi : {ligne_choisie}")
                    medicament_id = ligne_choisie.split()[0]
                    url_modifiee = modifier_url(medicament_id)
                    print(f"L'URL modifiée est : {url_modifiee}")
                    effets_indis = recuperer_effets_indesirables(url_modifiee)
                    print(f"Effets indésirables : {effets_indis}")
                else:
                    print("Numéro de choix invalide.")
            else:
                print("Veuillez fournir un numéro de choix pour le médicament.")
        else:
            print(f"Le médicament '{medicament_a_rechercher}' n'a pas été trouvé dans la liste.")

    elif args.choix == 2:
        symptome = args.parametre
        resultats_symptomes = rechercher_medicament_par_symptome2(symptome, fichier_effets_secondaires)

        if resultats_symptomes:
            print(f"Médicaments potentiels pour le symptôme '{symptome}':")
            for i, medicament in enumerate(resultats_symptomes):
                print(f"{i + 1}. {medicament}")
        else:
            print(f"Aucun médicament trouvé pour le symptôme '{symptome}'.")

    elif args.choix == 3:
        symptome_a_evaluer = args.parametre
        phrase = generer_phrase(symptome_a_evaluer)
        print(f"Voici la phrase à copier : {phrase}")

    elif args.choix == 4:
        liste_medicaments_input = args.parametre.split(',')
        liste_medicaments = [med.strip() for med in liste_medicaments_input]
        symptome = args.symptome 
        
        if symptome:
            resultats = rechercher_medicaments_et_symptomes(liste_medicaments, symptome, fichier_effets_secondaires)
            print(f"Résultats pour le symptôme '{symptome}' :")
            for resultat in resultats:
                print(resultat)
        else:
            print("Veuillez fournir un symptôme avec l'option --symptome.")

    else:
        print("Option invalide, veuillez choisir 1, 2, 3, 4 ou 5.")

if __name__ == "__main__":
    main()
