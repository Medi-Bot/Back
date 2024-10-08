def rechercher_mot_dans_fichier(fichier, mot):
    with open(fichier, 'r', encoding='utf-8') as f:
        lignes = f.readlines()
        lignes_trouvees = []
        for ligne in lignes:
            # Enlever les espaces en début et fin de ligne
            ligne = ligne.strip()
            if mot.lower() in ligne.lower():
                lignes_trouvees.append(ligne)
    
    return lignes_trouvees

medicament_a_rechercher = input("Entrez le mot à rechercher : ")

fichier_liste_medicaments = 'Medicaments.txt'

resultats = rechercher_mot_dans_fichier(fichier_liste_medicaments, medicament_a_rechercher)

# Affichage des résultats
if resultats:
    print(f"Les médicaments trouvés contenant '{medicament_a_rechercher}' :")
    for i, resultat in enumerate(resultats):
        print(f"{i + 1}. {resultat}")

    choix = input("Entrez le numéro du médicament que vous souhaitez sélectionner : ")

    try:
        choix_numero = int(choix) - 1
        if 0 <= choix_numero < len(resultats):
            print(f"Vous avez choisi : {resultats[choix_numero]}")
        else:
            print("Numéro de choix invalide.")
    except ValueError:
        print("Veuillez entrer un numéro valide.")
else:
    print(f"Le médicament '{medicament_a_rechercher}' n'a pas été trouvé dans la liste.")
