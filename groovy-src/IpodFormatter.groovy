import org.cmc.music.metadata.IMusicMetadata
import org.cmc.music.metadata.ImageData
import org.cmc.music.metadata.MusicMetadataSet
import org.cmc.music.myid3.MyID3

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 4 avr. 2009
 * Time: 23:26:21
 */

parseFolder(new File('enter/here/your/absolute/input/path'))
//parseFolder(new File('/tmp/ID3'))

void parseFolder(File folder) {
  println '[parseFolder] parsing ' + folder
  MyID3 id3 = new MyID3()

  if (!folder.exists()) {
    println '[parseFolder] non-existing folder ' + folder.absolutePath
    System.exit(1)
  }

  ImageData cover = null
          

  /* looking for cover and parsing */
  folder.eachFileMatch(~/^Cover.(jpeg|jpg|png)$/) {
    FileInputStream fis = new FileInputStream((File) it)
    def byte[] content = new byte[fis.available()]
    fis.read(content, 0, content.length)
    fis.close()
    // http://www.id3.org/d3v2.3.0
    if (it.name.endsWith('.png'))
      cover = new ImageData(content, 'image/png', 'Injected w. IpodFormatter', 3)
    else
      cover = new ImageData(content, 'image/jpg', 'Injected w. IpodFormatter', 3)
  }
  folder.eachFile {  // for each file of the directory
    if (it.isDirectory()) {
      return parseFolder(it)  // recursive call
    }
    if (cover != null && it.name.endsWith('.mp3')) { // image tag injection
      MusicMetadataSet mms = id3.read(it)
      IMusicMetadata data = mms?.id3v2Raw?.values // simplified or id3v2clean don't work (see SimpleParsing)

      data?.addPicture(cover) // if data != null
      try {
        if (data != null)
          id3.update(it, mms, data)    // todo : on-fly copy from read only source ? (id3.write)
      }
      catch (Exception x) {
        println '[parseFolder] exception ' + x.class.name + ' w. file ' + it.name + ' :' + x.message
      }
    }
  }// end of main loop

  // erase all non mp3 files
  folder.eachFile {
    if (!it.name.endsWith('.mp3')) {
      // if its a file or an empty directory
      // (the empty directory thing works because of the recursive call made earlier)
      if (it.isFile() || (it.isDirectory() && it.list().length == 0)) {
        println '[deleteFile] ' + it.name
        it.delete()
      }
    }
  }
}//END of parseFolder



