; Switches to Winamp and sends "x" key to restart the stream
#SingleInstance force

#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
; #Warn  ; Enable warnings to assist with detecting common errors.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.

WinGetClass, class, A
WinActivate ahk_class Winamp v1.x
Send,x
Sleep,1
WinActivate ahk_class %class%
Return