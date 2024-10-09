DROP table IF EXISTS Poids;
DROP table IF EXISTS Informations;
DROP table IF EXISTS Antecedent;
DROP table IF EXISTS Taille;
DROP table IF EXISTS MedicamentUtilise;
DROP table IF EXISTS HistoriqueCommunication;

create table Poids(
    date text primary key,
    poids real
);

create table Informations(
    id int primary key,
    date_de_naissance text
);

create table Antecedent(
    date text primary key,
    description text
);

create table Taille(
    date text primary key,
    taille real
);

create table MedicamentUtilise(
    date_de_debut text,
    nom text,
    frequence text,
    date_de_fin text,
    primary key(date_de_debut, nom)
);

create table HistoriqueCommunication(
    date text primary key,
    message text,
    reponse text
);