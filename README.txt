     			README 

### --- Utilisation de l’Application sous Eclipse --- ###

Pour compiler notre programme il faut ajouter le module json-20190722.jar (https://mvnrepository.com/artifact/org.json/json/20190722) et le module mysql-connector-java-8.0.13.jar (https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.13). 
(Ils seront déposés avec le reste sur moodle) 

Avant tout : 
	1) Allumer la base de données (exemple activer WampServer) sur le port 3306

Pour créer la base de données : 
	1) lancer le programme “CreateDatabase” 
	2) lancer le programme “lancer” pour ajouter quelques données dans la base (ce programme vide d’abord les tables puis ajoute des données) 

Pour lancer l’interface utilisateur: 
	1) lancer le serveur avec la classe “Serveur” (située dans projet.serveur) si le Serveur n’a pas encore été lancé
	2) lancer l’interface avec la classe “InterfaceUtilisateur” (située dans projet.utilisateur.interfaces)

-> Vous pouvez par exemple vous connecter avec Login : U1000002 et password : 1234567 
-> Vous pouvez par exemple vous connecter avec Login : U1000001 et password : 1234
-> Vous pouvez par exemple vous connecter avec Login : U1000006 et password : 678  
(Vous retrouverez la liste des différents logins de chacun des utilisateurs ainsi que les mots de passe associés dans les tables de la Base de Données)

Pour lancer l’interface serveur :
	1) lancer le serveur avec la classe “Serveur” (située dans projet.serveur) si le Serveur n’a pas encore été lancé
	2) lancer l’interface avec la classe “InterfaceServeur” (située dans projet.serveur.interfaces)


Pour lancer l’interface générale : 
	1) lancer le serveur avec la classe “Serveur” (située dans projet.serveur)
	2) lancer l’interface avec la classe “InterfaceMain” (située dans projet.main)

-> Pour se connecter en tant qu’administrateur du serveur il vous suffit d’utiliser ces identifiants : login = admin et mdp = mdp 



### --- Utilisation de l’Application depuis l’exécutable --- ###

Liste des exécutables : 
 - LancerServeur.jar: correspond au lancement du serveur 
 - CreationBaseDeDonnees.jar: correspond à la création de la database 
 - InitialisationBase.jar: correspond au remplissement de la base de données 
 - ProjetS5.jar : correspond à l'interface qui permet de choisir entre le serveur et un utilisateur 


Il faut d'abord lancer l'exécutable du Serveur. 

Lors du lancement de l’application, vous accéderez à l’interface de choix de profil : administrateur ou utilisateur. Choisissez le profil adapté pour pouvoir accéder à l’interface de connexion.

Interface Administrateur
Une fois connecté à l’espace administrateur, vous accéder à un menu vous permettant de créer un nouvel utilisateur, un nouveau groupe, affecter un utilisateur à un groupe, retirer un utilisateur d’un groupe ou bien tout simplement supprimer un utilisateur ou un groupe.

Interface Utilisateur
Une fois connecté à votre espace utilisateur, vous pouvez accéder à l’ensemble des tickets vous concernant sur le panel de droite. Pour lire l’une des conversations, il vous suffit de cliquer sur le groupe souhaité, puis sur le ticket souhaité pour pouvoir accéder à la conversation s’affichant sur le panel de droite.
Une fois un ticket ouvert, vous pouvez ajouter un message au fil de discussion en saisissant votre message dans l’espace correspondant puis en cliquant sur le bouton “Envoyer”.
Vous avez la possibilité de créer un nouveau ticket, en indiquant le groupe destinataire ainsi que le sujet du ticket en cliquant sur le bouton “+Ticket” situé en haut à gauche de votre interface.
Une bouton de rafraîchissement est également utilisable au même endroit, enfin de récupérer les derniers messages envoyés ou reçus.

À noter que vous pouvez vous déconnecter et fermer l’application à tout moment en fermant l’application, quel que soit le profil d’utilisation connecté.




### --- Autre manière de création de la Base de Données --- ###

Si vous ne souhaitez pas utiliser la fonction de création de la base de données initiale, vous pouvez également utiliser une base de données vierge.
Votre nouvelle base de données devra contenir 7 tables distinctes :
	- groupeutilisateur : contenant 2 colonnes (nomGroupe CHAR(255), identifiantUtilisateur CHAR(255))
	- listegroupes : contenant 1 colonne (nomGroupe CHAR(255))
	- messages : contenant 5 colonnes (identifiantMessage CHAR(255), identifiantTicket CHAR(255), date TIMESTAMP, identite CHAR(255), message TEXT(1000)) 
	- messagesattente : contenant 2 colonnes (identifiantMessage CHAR(255), identifiantUtilisateur CHAR(255))
	- messagesattentevues : contenant 2 colonnes (identifiantMessage CHAR(255), identifiantUtilisateur CHAR(255))
	- tickets : contenant 5 colonnes (identifiantTicket CHAR(255), titre TEST, date TIMESTAMP, groupeEmetteur CHAR(255), groupeRecepteur CHAR(255))
	- utilisateurs : contenant 4 colonnes (identifiant CHAR(255), mdp CHAR(255), identite CHAR(255), statut CHAR(255))
