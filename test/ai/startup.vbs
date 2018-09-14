set ws =CreateObject("WScript.Shell")

Dim currentpath
currentpath=createobject("Scripting.FileSystemObject").GetFolder(".").Path


'新增
'ws.Environment("system").Item("mySoftware")= newPath
'修改
newPath = currentpath+"\jre1.8.0_181\bin"
ws.Environment("system").Item("Path")= ws.Environment("system").Item("Path") + ";"+newPath 

'rem envs("test_path_1") = "e:\test"

ws.Run "cmd /c java -jar test-0.0.1-SNAPSHOT.jar",0