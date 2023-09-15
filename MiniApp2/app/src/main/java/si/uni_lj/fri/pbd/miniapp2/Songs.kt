package si.uni_lj.fri.pbd.miniapp2

// Object that is used to interact with the songs and their data easier
// title -> the title of the mp3 file of the song
// fileR -> the name of the mp3 file that will be used to get it from the "raw" folder
// imgR -> the name of the jpg image that will be used to get it from the "drawable" folder
data class Song(val title: String, val fileR: String, val imgR: String)

// Object that contains list of all the songs that will be used in the application
// Got the idea from the 5th Lab Sessions
object Songs {
    val songList = listOf(
        Song(
            "Lord Huron-The Night We Met.mp3",
            "music1",
            "pic1"
        ),
        Song(
            "Imagine Dragons - Bones.mp3",
            "music2",
            "pic2"
        ),
        Song(
            "Sam Tinnesz - Play With Fire feat. Yacht Money.mp3",
            "music3",
            "pic3"
        ),
        Song(
            "2 Chainz & Wiz Khalifa - We Own It (Fast & Furious).mp3",
            "music4",
            "pic4"
        ),
    )
}