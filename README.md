# Useful commands for everything (adb)

- abd devices - list all devices using ADB (Android Debug Bridge)
- abd shell dumpsys battery - will give you the output of the battery information (run remote shell command)
- abd push file.txt mnt/SDcard/Download/file.txt - push a file from your computer to your device
- abd pull mnt/SDcard/Download/file.txt file.txt - pull a file from your device to your computer
- abd shell - open a shell on the device
- adb shell screencap /mnt/sdcard/Download/test.png - take a screenshot on the device
- adb pull /mnt/sdcard/Download/test.png test.png - pull the screenshot from the device to your computer
- adb shell ps - list all processes running on the device
- adb install file.apk - install an APK on the device
- adb shell pm list packages | grep 'lab' - list all packages that contain the word 'lab'
- adb shell pm list packages | grep 'weather' - list all packages that contain the word 'weather'
- adb shell am start -n com.MobileSecurity.lab1/com.MobileSecurity.labs.MainActivity - start an activity
- adb shell am start -n com.example.weatherapp/com.example.weatherapp.MainActivity - start an activity (my project)
- adb root - restart the adb daemon with root permissions
- adb pull /data/data/com.MobileSecurity.lab1/files/info.txt info.txt - default local storage files are located in /data/data/package_name
- adb logcat --pid=6013 - filter logcat by process ID (or simpler just read the logcat, of a specific app by PID)
- sqlite3 Userdata.db - open a SQLite database (if you have root access) (then you can do .tables and then for example - select * from users;)

# Rooting the device

- clone this repository: https://github.com/newbit1/rootAVD.git
- make sure you have a running emulator version sdk 34 "UpsideDownCake"
- run the script: ./rootAVD.sh ListAllAVDs
- run the script: ./rootAVD.sh system-images/android-34/google_apis/x86_64/ramdisk.img
- wait for success inside of the script
- after the script runs, you will have to start your AVD again, but this might fail, so I had to also reboot once and then have everything running again
- then download the rootchecker and install it via **adb install rootchecker.apk** (https://leho-howest.instructure.com/courses/20970/files/3782642/download?download_frd=1)
- restart adb with root permissions: **adb root**
- and then enter rootchecker, grant permissions and verify that you have root access

# Frida scripting

- first we need to install frida-tools: **sudo nala install pipx**; **pipx install frida-tools**; **pipx ensurepath**
- download frida-server from github: wget https://github.com/frida/frida/releases/download/16.2.1/frida-server-16.2.1-android-x86_64.xz (the architecture should match the one of the emulator)
- extract the file: **unxz frida-server-12.8.10-android-x86_64.xz**; **mv frida-server-12.8.10-android-x86_64 frida-server**
- push the file to the emulator: **adb push frida-server /data/local/tmp**
- change the permissions of the file: **adb shell chmod 755 /data/local/tmp/frida-server**
- start the server: **adb shell "/data/local/tmp/frida-server &"**
- Make sure that the frida client (tools, frida --v) is the same version as the server
- Then your application should be able to work, and you can try it out by doing for example: **frida-ps -Ua** to get the list of processes and find the class that you need
- Then you can write script like:
```javascript
// Frida script to bypass login credential check
Java.perform(function () {
  
const target = Java.use('com.example.weatherapp.ui.login.LoginScreenKt')
        target.checkLoginCredentials.implementation = function(x,y,z)
{
        console.log("Bypassing login");
        return true
    };
});
```
- And finally to launch it, you can try to do: **frida -U -l bypass_login.js -f com.example.weatherapp**
- How it works - if you know the username, you can put any password and it will let you in. But there are some bugs after it, and it will not log properly and the user variable will not be properly fitted.

# Important things to remember:

- from code to APK - in case of Java/Kotlin - **.java/.kt (java/kotlin compiler) -> .class/.jar (DEX Compiler) -> .dex (i.e Dalvik bytecode) -> .apk**
- after building and APK, you can unzip it and see the contents of it (so .apk is just a zip file = i.e. application.zip <-> application.apk)
- in android, we dont have JVM like for Java, hence we deal with DEX files, and those are already decompiled into smali code (which is a human-readable version of DEX files), and then you get decompiled java. (DEX -> Smali -> Decompiled Java)
- JADX - a tool to decompile APKs and see the source code

# BurpSuite

- To intercept the HTTP traffic, you need to set up the proxy on device (go to wifi settings, forget the AndroidWifi, connect again, set up manual proxy to 10.0.2.2 {i.e. default gateway} and port 8080).
- After this, you will be able to see http traffic in BurpSuite (make sure you have the proxy running on 8080)
- To be able to read HTTPS, we need SSL pinning, which is a way to get rid of MITM attacks, and to bypass it, we need to make some stuff.
- It is important to know that the certificates (trusted CA's) are stored in the system, '/system/etc/security/cacerts' and '/data/misc/keychain/cacerts-added' (the second one is for user-added certificates)
- With root privileges, we can add burp suite certificates such as that they will run properly for our Burp.
- Get the certificate from BurpSuite, and then: **openssl x509 -inform DER -in cacert.der -out cacert.pem** (to convert it to PEM format)
- Then: **openssl x509 -inform PEM -subject_hash_old -in cacert.pem |head -1** (to get the hash of the certificate)
- Then: **mv cacert.pem <hash>.0** (to rename the file to the hash)
- Then we need to save it in /system/etc/security/cacerts, but it is not writable 
- Go into **adb root; adb shell; mkdir -p /data/adb/modules/playstore/**
- Then: **adb push 9a5ba575.0 /data/adb/modules/playstore/system/etc/security/cacerts/9a5ba575.0** (this is all possible thanks to Magisk)
- Then, make sure that your wifi is still running, with the correct proxy, and then go to chrome, and try to ope **http://burp** and you should be able to download the CA certificate
- Then after you installed it, you should be able to see the HTTPS traffic in BurpSuite (after a reboot of course)