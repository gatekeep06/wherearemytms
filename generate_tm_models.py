import os, json

machines = ['tm', 'hm']

types = ['normal', 'fire', 'water', 'grass', 'electric', 
         'ice', 'fighting', 'poison', 'ground', 'flying', 
         'psychic', 'bug', 'rock', 'ghost', 'dragon',
         'dark', 'steel', 'fairy']

import os

WORKING_DIR = 'python_out'
if not os.path_exists(WORKING_DIR):
    os.makedirs(WORKING_DIR)

def initial_write(d):
    d['parent'] = 'item/generated'
    d['textures'] = {}

for m in machines:
    for t in types:
        data = {}
        with open(os.path.join(WORKING_DIR, f'{m}/{t}.txt'), 'w') as fp:
            initial_write(data)
            data['textures']['layer0'] = f"wherearemytms:item/{m}/{t}"
            json.dump(data, fp)