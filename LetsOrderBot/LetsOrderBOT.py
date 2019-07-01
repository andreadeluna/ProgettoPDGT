# Bot sviluppato da: Andrea De Luna
# Università degli Studi di Urbino Carlo Bo
# Progetto per la sessione estiva di PDGT


from settings import token, start_msg, client_file, url_api
from telepot.loop import MessageLoop
from telepot.namedtuple import ReplyKeyboardMarkup, ReplyKeyboardRemove, InlineKeyboardMarkup, InlineKeyboardButton
from time import sleep
import time
import json
import os
import requests
import reverse_geocode
import sys
import telepot


# Stato dell'utente
user_state = {}
place = {}
restaurant = {}
place_id = {}


def on_chat_message(msg):
    content_type, chat_type, chat_id = telepot.glance(msg)

    # Controllo dello stato dell'utente
    try:
        user_state[chat_id] = user_state[chat_id]
    except:
        user_state[chat_id] = 0

    # Comando start
    if 'text' in msg and msg['text'] == '/start':
        if register_user(chat_id):
            bot.sendMessage(chat_id, start_msg, parse_mode='Markdown')

    
    # Comando info
    if 'text' in msg and msg['text'] == '/info':

        bot.sendMessage(
            chat_id, "Bot creato da Andrea De Luna per il progetto di PDGT. Scarica anche l'app LetsOrder per ordinare direttamente da casa tua! 🏠🍕 ")
        

    # Comando cerca
    if 'text' in msg and msg['text'] == '/cerca':

        #Richiesta di inserimento della posizione
        bot.sendMessage(
            chat_id, 'Invia il luogo dove si trova il ristorante o la tua posizione')
        user_state[chat_id] = 2

    elif user_state[chat_id] == 2:
        if content_type == 'text':
            cerca(msg['text'], msg)

        if content_type == 'location':
            coordinates = (str(msg['location']['latitude']),
                           str(msg['location']['longitude'])),
            a = reverse_geocode.search(coordinates)[0]['city']

            cerca(a, msg)

    elif user_state[chat_id] == 3:

        # Se il ristorante compare nell'elenco
        if msg['text'] == 'Si':
            user_state[chat_id] = 4
            # Richiesta di inserimento del numero del ristorante
            bot.sendMessage(chat_id, 'Scrivi il numero del ristorante', reply_markup=ReplyKeyboardRemove(
                remove_keyboard=True))

        if msg['text'] == 'No':
            # Richiesta di inserimento del nome del ristorante
            bot.sendMessage(chat_id, 'Scrivi il nome del ristorante', reply_markup=ReplyKeyboardRemove(
                remove_keyboard=True))
            user_state[chat_id] = 5
    
    elif user_state[chat_id] == 4:
        richiesta(url_api + '?tipo=id&lista=' +
                  place_id[chat_id][int(msg['text'])-1], msg)

    elif user_state[chat_id] == 5:
        richiesta(url_api + "?tipo=diretto&lista=" +
                  msg['text'] + ' ' + place[chat_id], msg)


def richiesta(url, msg):
    content_type, chat_type, chat_id = telepot.glance(msg)

    try:
        r = requests.get(
            url=url)
        json_data = r.json()

        file = open("__pycache__/"+str(chat_id)+".json", "w")
        file.write(r.text)
        file.close()

        # Creazione dell'array con i dati dei ristoranti
        nome = json_data['lista'][0]['nome']
        orari = json_data['orari'][0]
        posizione = json_data['lista'][0]['posizione'].split(',')
        apertura = json_data['lista'][0]['apertura']
        numtell = json_data['lista'][0]['numtell']
        valutazione = json_data['lista'][0]['valutazione']

        if orari == None:
            orari = 'Orari non disponibili'
        else:
            orari = '\n'.join(orari)

        restaurant[chat_id] = nome

        keyboard = InlineKeyboardMarkup(inline_keyboard=[
            [dict(text='Feedback di Google', callback_data=1)]
        ])


        # Se il ristorante è aperto
        if apertura == 'Aperto' or apertura == None:
            bot.sendMessage(chat_id, '🍽 ' + nome + '\n🕐 ' + str(apertura).replace('None', 'Apertura non disponibile') +
                            '\n📱 ' + str(numtell) + '\n⭐️ ' + str(valutazione))

        # Se il ristorante è chiuso
        else:
            bot.sendMessage(chat_id, '🍽 ' + nome + '\n🕐 ' + str(apertura).replace('None', 'Apertura non disponibile') +
                            '\n📱 ' + str(numtell) + '\n⭐️ ' + str(valutazione) + '\n------\n' + orari)

        bot.sendLocation(chat_id, posizione[0], posizione[1])
        bot.sendMessage(
            chat_id, "Hai bisogno di aiuto?", reply_markup=keyboard)
        user_state[chat_id] = 0

    except:
        print("Errore API richiesta")
        bot.sendMessage(
            chat_id, 'Errore nella richiesta dati, rieffettua la ricerca 🔍')
        




# Ricerca del ristorante
def cerca(luogo, msg):
    content_type, chat_type, chat_id = telepot.glance(msg)
    r = requests.get(
        url=url_api + '?tipo=luogo&lista=' + luogo.lower())
    try:
        json_data = r.json()
        place[chat_id] = luogo.lower()

        array = ''
        array1 = ''
        for i in range(5):
            nome = json_data['lista'][i]['nome']
            placeid = json_data['lista'][i]['id']
            array += str(i + 1) + ': ' + nome + '\n'
            array1 += placeid + ','

        place_id[chat_id] = array1.split(',')

        markup = ReplyKeyboardMarkup(keyboard=[["Si", "No"]])
        bot.sendMessage(
            chat_id, array + "\nIl ristorante è nella lista?", reply_markup=markup)

        user_state[chat_id] = 3
    except:
        bot.sendMessage(
            chat_id, "Locali non trovati/ Errore interno API, riscrivi il nome del locale")


# Visualizzazione recensioni
def on_callback_query(msg):
    query_id, from_id, query_data = telepot.glance(
        msg, flavor='callback_query')
    edited = (from_id, msg['message']['message_id'])
    try:

        file = open("__pycache__/"+str(from_id)+".json", "r")
        json_data = json.load(file)

        if (query_data == str(1)):

            lista = json_data['feedback'][int(query_data)-1]
            autore = lista['author_name']
            valutazione = str(lista['rating'])
            commento = lista['text']
            feedback = 'Autore: ' + autore + '\nValutazione: ' +\
                valutazione + '\nFeedback: ' + commento

            keyboard = InlineKeyboardMarkup(inline_keyboard=[
                [dict(text='Avanti', callback_data=2)]

            ])

            bot.editMessageText(edited, feedback,
                                reply_markup=keyboard)

        if (query_data == str(2)):
            keyboard = InlineKeyboardMarkup(inline_keyboard=[
                [dict(text='Indietro', callback_data=1),
                 dict(text='Avanti', callback_data=3)]
            ])

            lista = json_data['feedback'][int(query_data)-1]
            autore = lista['author_name']
            valutazione = str(lista['rating'])
            commento = lista['text']
            feedback = 'Autore: ' + autore + '\nValutazione: ' +\
                valutazione + '\nFeedback: ' + commento

            bot.editMessageText(edited, feedback,
                                reply_markup=keyboard)

        if (query_data == str(3)):
            keyboard = InlineKeyboardMarkup(inline_keyboard=[
                [dict(text='Indietro', callback_data=2),
                 dict(text='Avanti', callback_data=4)]
            ])

            lista = json_data['feedback'][int(query_data)-1]
            autore = lista['author_name']
            valutazione = str(lista['rating'])
            commento = lista['text']
            feedback = 'Autore: ' + autore + '\nValutazione: ' +\
                valutazione + '\nFeedback: ' + commento

            bot.editMessageText(edited, feedback,
                                reply_markup=keyboard)

        if (query_data == str(4)):
            keyboard = InlineKeyboardMarkup(inline_keyboard=[
                [dict(text='Indietro', callback_data=3),
                 dict(text='Avanti', callback_data=5)]
            ])

            lista = json_data['feedback'][int(query_data)-1]
            autore = lista['author_name']
            valutazione = str(lista['rating'])
            commento = lista['text']
            feedback = 'Autore: ' + autore + '\nValutazione: ' +\
                valutazione + '\nFeedback: ' + commento

            bot.editMessageText(edited, feedback,
                                reply_markup=keyboard)

        if (query_data == str(5)):
            keyboard = InlineKeyboardMarkup(inline_keyboard=[
                [dict(text='Indietro', callback_data=4),
                 dict(text='Fine', callback_data=6)]
            ])

            lista = json_data['feedback'][int(query_data)-1]
            autore = lista['author_name']
            valutazione = str(lista['rating'])
            commento = lista['text']
            feedback = 'Autore: ' + autore + '\nValutazione: ' +\
                valutazione + '\nFeedback: ' + commento

            bot.editMessageText(edited, feedback,
                                reply_markup=keyboard)

        if (query_data == str(6)):
            bot.editMessageText(edited, 'Fine')

            keyboard = InlineKeyboardMarkup(inline_keyboard=[
                [dict(text='Visualizza feedback di Google', callback_data=1)]
            ])
            bot.editMessageText(
                edited, "Hai bisogno di aiuto?", reply_markup=keyboard)

    except Exception as e:
        print(e)


def register_user(chat_id):
    """
    Register user
    """
    insert = 1

    try:
        f = open(client_file, "r+")

        for user in f.readlines():
            if user.replace('\n', '') == str(chat_id):
                insert = 0

    except IOError:
        f = open(client_file, "w")

    if insert:
        f.write(str(chat_id) + '\n')

    f.close()

    return insert


# Main
print("Avvio LetsOrderBot")

# PID file
pid = str(os.getpid())
pidfile = "tmp\LetsOrderBOT.pid"

# Check if PID exist
if os.path.isfile(pidfile):
    print("%s already exists, exiting!" % pidfile)
    sys.exit(0)

# Create PID file
f = open(pidfile, 'w')
f.write(pid)

# Start working
try:
    bot = telepot.Bot(token)
    MessageLoop(bot, {'chat': on_chat_message,
                      'callback_query': on_callback_query}).run_as_thread()
    while(1):
        sleep(10)
finally:
    os.unlink(pidfile)