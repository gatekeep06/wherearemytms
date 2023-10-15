import os, json

machines = ['tm', 'hm']

types = ['normal', 'fire', 'water', 'grass', 'electric', 
         'ice', 'fighting', 'poison', 'ground', 'flying', 
         'psychic', 'bug', 'rock', 'ghost', 'dragon',
         'dark', 'steel', 'fairy']

WORKING_DIR = 'python_out'
if not os.path.exists(WORKING_DIR):
    os.makedirs(WORKING_DIR)

def initial_write(d):
    d['parent'] = 'item/generated'
    d['textures'] = {}

def blank_write(d):
    d['parent'] = 'item/generated'
    d['textures'] = {}

for m in machines:
    new_path = os.path.join(WORKING_DIR, m)
    if not os.path.exists(new_path):
        os.makedirs(new_path)

    blank = {}
    with open(os.path.join(new_path, f'blank_{m}.json'), 'w') as bfp:
        initial_write(blank)
        blank['overrides'] = []
        blank['textures']['layer0'] = f'wherearemytms:item/{m}/blank'
    
        for bt in types:
            l = {}
            l['predicate'] = {f'wherearemytms:{bt}': 1}
            l['model'] = f'wherearemytms:item/{m}/{bt}'
            blank['overrides'].append(l)

        json.dump(blank, bfp, indent=4)



    for t in types:
        data = {}
        with open(os.path.join(new_path, f'{t}.json'), 'w') as fp:
            initial_write(data)
            data['textures']['layer0'] = f"wherearemytms:item/{m}/{t}"
            json.dump(data, fp, indent=4)