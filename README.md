Features:


This application supports different roles such as admin, regular user, and artist.
Users can only register as a regular user or an artist.
An admin is predefined by default, and can log in with the ID "admin" and password "0000".
Artists and admins can add new songs, and users can request them to edit song lyrics.


Structure:


___________________


Classes:


* `User`: Base class for system users.
* `Artist`: Subclass of `User`, storing the list of songs for each artist.
* `Admin`: Subclass of `User` for system administrators.
* `Song`: Song model with title, lyrics, and artist.
* `LyricEditRequest`: Model for lyric edit requests.
* `DataService`: Central service for managing users, songs, and requests.
* `LyricEditor`: Manages the registration and approval of lyric edit requests.


____________________


Functions and Methods:


Password hashing function.  `Objects.Hash()` is used for enhanced security to avoid storing the original password. The entered password is compared with the stored hash to verify its correctness.
`createSong()`: Only ARTISTS or ADMINS can create songs. If it's an ADMIN, they must select an artist.
`initializeSampleAdmin`: Function to create the default admin.
`editSongLyrics()`: Regular users can only request edits; artists or admins can directly update the lyrics.
`showEditRequests()`: Only ADMINs or ARTISTS can view requests.
`listAllSongs()`: Lists all songs (or only the artist's own songs).
`readInt()`: A helper function to get a number from the user.  It doesn't throw an error if the input is not a number; instead, it displays a message asking the user to enter a number.
`register()`: Used for registration; only regular users and artists can register.
`login()`: Used to log in to the application; it receives the password and ID from the user and compares them with the stored information.
________


This project only uses the `java.lang` and `java.util` libraries.
