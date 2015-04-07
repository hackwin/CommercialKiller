# CommercialKiller
This is my attempt to detect and bypass radio commercials.

Audio from stereo mix is stored in a 5 second buffer.
Every half a second, more audio is put into the queue buffer and the old audio is dequeued (FIFO).  

After new data is collected, commercials are searched for in the buffer.  A commercial is a subset of bytes in the playback buffer.

If you hear a commercial, you can double right click and it will save the last five seconds
Run this first: killCommercialsAutomatically.ahk

Requires Winamp and AutoHotkey
Radionomy stations: http://www.jbcse.com/downloads/radionomy.m3u
