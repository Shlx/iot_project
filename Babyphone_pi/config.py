from Tkinter import *


def close():
    master.quit()
    master.destroy()
    
def save():
    un = username.get()
    pw = password.get()
    if (len(un) >= 8) and (len(pw) == 16):
        configfile = file("config.monitor", "w")
        configfile.write("username=" + un + "\n")
        configfile.write("password=" + pw + "\n")
        configfile.close()
    master.quit()
    master.destroy()


master = Tk()
Label(master, text="Username").grid(row=0)
Label(master, text="Passwort").grid(row=1)

username = Entry(master)
password = Entry(master)

username.grid(row=0, column=1)
password.grid(row=1, column=1)

Button(master, text='Beenden', command=close).grid(row=3, column=0, sticky=W, pady=4)
Button(master, text='Speichern', command=save).grid(row=3, column=1, sticky=W, pady=4)

mainloop( )
