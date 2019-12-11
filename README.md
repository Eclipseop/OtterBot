# OtterBot
OtterBot is a Java discord bot, using JDA.

## Commands
* Music
    * play {*url*}: Plays a specific video.
    * play {*text*}: Searches for a video with the given query.
    * play {*1-5*}: Plays a specific song from song selection.
    * volume {*num*}: Sets the volume of the player. Will Reset after every song.
    * playing: Shows the current song.
    * skip: Skips the current song.
    * stop: Stops all playback and clears the queue.
    * queue: Shows the top 5 songs in the queue.
    * remove {*1-5*}: Removes the nth song from the queue.
    * playing: Shows the current song playing.
    * earrape: Turns on earrape for the current song.
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
