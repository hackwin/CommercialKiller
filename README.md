# CommercialKiller
This program is an attempt to detect and bypass radio commercials.

Audio from stereo mix is stored in a 5 second buffer.
Every half a second, more audio is put into the queue buffer and the oldest audio is dequeued (FIFO).  

After new data is collected, commercials are searched for in the queue buffer.  A commercial detected as a subset of bytes in the playback buffer queue data.

If you hear a commercial that isn't saved already, you can double right click and it will save the last five seconds
Run this first: killCommercialsAutomatically.ahk

Requires Winamp and AutoHotkey

Radionomy stations: http://www.jbcse.com/downloads/radionomy.m3u
