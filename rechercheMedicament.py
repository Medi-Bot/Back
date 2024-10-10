from effets_indesirables import recuperer_effets_indesirables

def rechercher_mot_dans_fichier(fichier, mot):
    with open(fichier, 'r', encoding='utf-8') as f:
        lignes = f.readlines()
        lignes_trouvees = []

        for ligne in lignes:
            ligne = ligne.strip()

            if mot.lower() in ligne.lower():
                lignes_trouvees.append(ligne)
    
    return lignes_trouvees

def modifier_url(specid):
    base_url = "https://base-donnees-publique.medicaments.gouv.fr/affichageDoc.php?specid={}&typedoc=N#Ann3bEffetsIndesirables"
    nouvelle_url = base_url.format(specid)  # Remplacer {} par le specid
    return nouvelle_url

medicament_a_rechercher = input("Entrez le mot à rechercher : ")
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
            medicament_id = ligne_choisie.split()[0]  # Assure-toi que l'ID est bien en première position
            id_medicament = int(medicament_id)

            # Modifier l'URL avec l'ID trouvé
            url_modifiee = modifier_url(medicament_id)
            print(f"L'URL modifiée est : {url_modifiee}")

            effets = recuperer_effets_indesirables(url_modifiee)

        else:
            print("Numéro de choix invalide.")
    except ValueError:
        print("Veuillez entrer un numéro valide.")
else:
    print(f"Le médicament '{medicament_a_rechercher}' n'a pas été trouvé dans la liste.")
