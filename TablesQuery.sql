CREATE TABLE Library(
Id INT NOT NULL IDENTITY(1000, 1) PRIMARY KEY,
Name NVARCHAR(50) NOT NULL,
Type NVARCHAR(25) NOT NULL,
Descrip NVARCHAR(MAX) NOT NULL
)

CREATE TABLE MyList(
Id INT NOT NULL IDENTITY(10, 1),
ProductId INT NOT NULL FOREIGN KEY REFERENCES Library(Id),
Status NVARCHAR(10) NOT NULL
)

INSERT INTO dbo.Library
VALUES 
('Demon Slayer', 'Manga', 'It follows teenage Tanjiro Kamado, who joins the Demon Slayer Corps after his family is slaughtered and the sole survivor, his younger sister Nezuko, is turned into a demon, in the hopes of turning her human again and defeating the demon king Muzan Kibutsuji.'),
('One Piece', 'Anime', 'ONE PIECE is a legendary high-seas quest unlike any other. Luffy is a young adventurer who has longed for a life of freedom ever since he can remember. He sets off from his small village on a perilous journey to find the legendary fabled treasure, ONE PIECE, to become King of the Pirates!'),
('Dune: Part 2', 'Movie', 'Dune: Part Two follows Paul Atreides as he unites with the Fremen to seek revenge against the conspirators who destroyed his family, facing a choice between love and the fate of the universe.'),
('Sunrise on the Reaping', 'Book', 'Sunrise on the Reaping, a 2025 dystopian novel by Suzanne Collins, is a prequel to "The Hunger Games" trilogy, focusing on a young Haymitch Abernathy experience during the 50th Hunger Games, a Quarter Quell, where twice as many tributes are taken from each district'),
('Smallville', 'Show', 'Smallville is a superhero television series following the teenage years of Clark Kent in the fictional town of Smallville, Kansas, as he learns to control his emerging alien powers and explores his destiny before becoming Superman.'),
('Split Fiction', 'VideoGame', 'Embrace mind-blowing moments as you’re pulled deep into the many worlds of Split Fiction, a boundary-pushing co-op adventure.')