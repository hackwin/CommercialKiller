; For canceling commercials that come from Radionomy
; Must play internet radio using Winamp
; Playlist: http://www.jbcse.com/downloads/radionomy.m3u
; Double right click the mouse and it will cancel the commercial, and save the commercial using the CommercialKiller java program (run.bat), and return you to your window

#Persistent
#SingleInstance force

#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
; #Warn  ; Enable warnings to assist with detecting common errors.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.

;Double Right Click
~RButton::
If (A_PriorHotKey = A_ThisHotKey and A_TimeSincePriorHotkey < 350)
{
WinGetClass, class, A
WinActivate ahk_class Winamp v1.x
Send,x
Sleep,1
WinActivate C:\Windows\system32\cmd.exe ; run.bat
Send,s
Send,{Enter}
WinActivate ahk_class %class%
}
Return
