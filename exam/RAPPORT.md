# Rapport Exam Grégoire Launay--Bécue

Je n'ais pas eu le temps de faire certaine implémentation, notament certain post & update, mais la structure backend est la.

## Installation

docker compose up -d
maven install

## Endpoints

`[GET] /user/profile` : permet d'afficher le profil de l'utilisateur connecter
`[GET] /user/workout` : permet de récupérer les activité d'un utilisateur
`[GET] /user/login` : permet de se connecter
`[GET] /user/logout` : permet de ce déconnecter
`[GET] /user/profile` :
`[GET] /user/profile` :
`[GET] /user/profile` :

## Class

Utilisateur
-> sexe: enum
-> age: int
-> taille: int
-> poids: double

Sport
-> nom: String

Workout
-> durrée (s): double # car certain sport doivent être précis ex ski, natation
-> distance (m): double
-> calculCalori()

Follower
->