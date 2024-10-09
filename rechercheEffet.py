def rechercher_medicament_par_symptome(symptome, fichier):
    resultats = []
    try:
        with open(fichier, 'r', encoding='utf-8') as f:
            lignes = f.readlines()
            medicament = None 
            for i in range(len(lignes)):
                ligne = lignes[i].strip() 
                if ligne.startswith("Médicament:"):
                    medicament = ligne
                    print(f"Trouvé : {medicament}") 
                if medicament and 'Effets secondaires :' in ligne:
                    if i + 1 < len(lignes):
                        effets_secondaires = lignes[i + 1].strip()
                        print(f"Effets secondaires pour {medicament} : {effets_secondaires}") 
                        if symptome.lower() in effets_secondaires.lower():
                            resultats.append(medicament)
                            print(f"Symptôme trouvé dans : {medicament}") 
                            medicament = None 
    except FileNotFoundError:
        print(f"Le fichier {fichier} est introuvable.")
    
    return resultats

