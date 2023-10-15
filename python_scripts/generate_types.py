import os, json

machines = ['tm', 'hm']

types = ['normal', 'fire', 'water', 'grass', 'electric', 
         'ice', 'fighting', 'poison', 'ground', 'flying', 
         'psychic', 'bug', 'rock', 'ghost', 'dragon',
         'dark', 'steel', 'fairy']

WORKING_DIR = 'python_out'
if not os.path.exists(WORKING_DIR):
    os.makedirs(WORKING_DIR)

with open(os.path.join(WORKING_DIR, f'types.txt'), 'w') as fp:
    s = ''
    for t in types:
        s += f'types.put("{t}", "{t}");\n'
    fp.write(s)
