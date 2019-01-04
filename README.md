# ipod_formatter

When I received an Ipod Touch as a gift, I was disapointed because the device needs music in a very specific format to present it prettily.
For instance the Ipod could only present the album cover if the picture is injected into the .mp3 as a id3 tag.
Personnaly, I prefer the cover to be a single file cover.[jpg, png] in the same folder as the album.

Another example: I like to keep in the album folder a playlist (.m3u) file to quickly play the whole album.
But on the ipod, the file was messing up the collection and made tracks appears twice.


Therefore I started to write a small Groovy script to inject the cover into the .mp3 files and remove m3u files in a directory hierarchy. This is the file IpodFormatter.groovy that uses the "myid3" library to modify the tag.

Afterwards, I got other requirements and I wrote a new version in Java that uses the "jaudiotagger" library. The enty point of this programm is net.nino.ipodformat.Launcher.

It :
- remove empty dirs
- inject the cover as id3 tag
- delete non-mp3 files
- inject a special tag from Itunes for compilation albums, i.e. albums with multiple artists.

I leave this code for the posterity.
Please note that it's pretty old so I won't develop it anymore (I broke the Ipod anyway :0)).
