# OtterBot
OtterBot is a Java discord bot, using JDA.

## Commands
* Music
    * play(*url*) - Plays the url provided,
    * play(*search*) - Uses Youtube API and returns a search result.
    * play(*1-5*) - After using `play(search)`, selects a song from the search result.
* Path of Exile
    * deal(*chaos*) - Returns all possible deals within price range.
    * deal(*item name*) - Provides specific information about a deal.
    * price(*item name*) - Returns the price of a specific item.
    
## Dependencies
* [JDA](https://github.com/DV8FromTheWorld/JDA)
* [Gson](https://github.com/google/gson)
* [LavaPlayer](https://github.com/sedmelluq/lavaplayer)
* [YouTube API](https://developers.google.com/youtube/v3/)
* [Guava](https://github.com/google/guava)
* [Log4j](https://logging.apache.org/log4j/2.x/)
