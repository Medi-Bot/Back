#Utiliser pour le programme IA
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

#Utiliser pour le programme IA_argument
def rechercher_medicament_par_symptome2(symptome, fichier_effets_secondaires):
    resultats = []
    try:
        with open(fichier_effets_secondaires, 'r', encoding='utf-8') as f:
            lignes = f.readlines()
            for i in range(len(lignes)):
                # Recherchez si le symptôme apparaît dans la ligne actuelle
                if 'Effets secondaires :' in lignes[i]:
                    try:
                        effets_dict = eval(lignes[i].split("Effets secondaires :")[1].strip())
                        for frequence, effets in effets_dict.items():
                            for effet in effets:
                                if symptome.lower() in effet.lower():
                                    # Si un médicament est trouvé pour le symptôme
                                    medicament = lignes[i-1].strip()  # Supposons que le nom du médicament soit juste au-dessus
                                    resultats.append(medicament)
                                    break
                    except (IndexError, SyntaxError):
                        continue
    except FileNotFoundError:
        print(f"Le fichier {fichier_effets_secondaires} est introuvable.")
    
    return resultats

