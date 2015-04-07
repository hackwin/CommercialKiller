# CommercialKiller
This is my attempt to detect and bypass radio commercials.

Audio from stereo mix is stored in a 5 second buffer.
Every half a second, more audio is put into the queue buffer and the old audio is dequeued (FIFO)

If you hear a commercial, you can run double click and it will save the last five seconds
Running this: killCommercialsAutomatically.ahk

Requires Winamp and AutoHotkey
Radionomy stations: http://www.jbcse.com/downloads/radionomy.m3u
