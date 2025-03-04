CREATE TABLE `equipe` (
  `id_equipe` int(11) NOT NULL AUTO_INCREMENT,
  `nomEquipe` varchar(255) NOT NULL,
  `nbrEmployee` int(11) DEFAULT NULL,
  `nomTeqlead` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_equipe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `projet` (
  `id_projet` int(11) NOT NULL AUTO_INCREMENT,
  `nom_projet` varchar(255) NOT NULL,
  `Deadline` date DEFAULT NULL,
  `manager` varchar(255) DEFAULT NULL,
  `nom_client` varchar(255) DEFAULT NULL,
  `id_equipe` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_projet`),
  FOREIGN KEY (`id_equipe`) REFERENCES `equipe`(`id_equipe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci; 