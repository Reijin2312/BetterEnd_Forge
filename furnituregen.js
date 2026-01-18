const path = require("path")
const fs = require("fs")

const woods = ["pythadendron", "end_lotus", "lacugrove", "dragon_tree", "tenanea","helix_tree", "umbrella_tree", "jellyshroom", "lucernia", "mossy_glowshroom", "lucernia_jellyshroom"]
const woodDE = ["Pythadendron", "Endlotus", "Lacugrove", "Drachenbaum", "Tenanea","Spiralbaum", "Schirmbaum", "Geleepilz", "Lucernia", "Glühmospilz", "Gelelucernia"]
const woodEN = ["Pythadendron", "End Lotus", "Lacugrove", "Dragon Tree", "Tenanea","Helix Tree", "Umbrella Tree", "Jellyshroom", "Lucernia", "Mossy Glowshroom", "Jelly Lucernia"]

const nameDE = "Hängendes {wood}schild"
const nameEN = "Hanging {wood} Sign"
woods.forEach(wood => {
    const basePath = "./src/main/resources/assets/betterend";    

    fs.writeFileSync(path.join(basePath, "blockstates", `${wood}_bar_stool.json`), `{
  "variants": {
    "facing=east": {
      "model": "betterend:block/${wood}_bar_stool"
    },
    "facing=west": {
        "model": "betterend:block/${wood}_bar_stool",
        "y": 180
    },
    "facing=south": {
        "model": "betterend:block/${wood}_bar_stool",
        "y": 90
    },
    "facing=north": {
        "model": "betterend:block/${wood}_bar_stool",
        "y": 270
    }
  }
}`)

fs.writeFileSync(path.join(basePath, "blockstates", `${wood}_chair.json`), `{
    "variants": {
      "facing=east,top=false": {
        "model": "betterend:block/${wood}_chair",
        "y": 90
      },
      "facing=west,top=false": {
        "model": "betterend:block/${wood}_chair",
        "y": 270
      },
      "facing=south,top=false": {
        "model": "betterend:block/${wood}_chair",
        "y": 180
      },
      "facing=north,top=false": {
        "model": "betterend:block/${wood}_chair"
      },
      "top=true": {
        "model": "betterend:block/${wood}_chair_top"
      }
    }
  }`)

  fs.writeFileSync(path.join(basePath, "blockstates", `${wood}_taburet.json`), `{
    "variants": {
      "facing=east": {
        "model": "betterend:block/${wood}_taburet"
      },
      "facing=west": {
        "model": "betterend:block/${wood}_taburet",
        "y": 180
      },
      "facing=south": {
        "model": "betterend:block/${wood}_taburet",
        "y": 90
      },
      "facing=north": {
        "model": "betterend:block/${wood}_taburet",
        "y": 270
      }
    }
}`)



fs.writeFileSync(path.join(basePath, "models", "block", `${wood}_bar_stool.json`), `{
    "parent": "bclib:block/bar_stool",
    "textures": {
      "texture": "betterend:block/${wood}_planks",
      "cloth": "block/brown_wool"
    }
}`)

fs.writeFileSync(path.join(basePath, "models", "block", `${wood}_chair.json`), `{
    "parent": "bclib:block/chair",
    "textures": {
      "texture": "betterend:block/${wood}_planks"
    }
}`)

fs.writeFileSync(path.join(basePath, "models", "block", `${wood}_chair_top.json`), `{
    "textures": {
      "particle": "betterend:block/${wood}_planks"
    }
  }
  `)

fs.writeFileSync(path.join(basePath, "models", "block", `${wood}_taburet.json`), `{
    "parent": "bclib:block/taburet",
    "textures": {
      "texture": "betterend:block/${wood}_planks"
    }
}`)

fs.writeFileSync(path.join(basePath, "models", "item", `${wood}_bar_stool.json`), `{
    "parent": "betterend:block/${wood}_bar_stool"
  }`)

fs.writeFileSync(path.join(basePath, "models", "item", `${wood}_chair.json`), `{
    "parent": "betterend:block/${wood}_chair",
    "display": {
      "gui": {
        "rotation": [
          30,
          45,
          0
        ],
        "translation": [
          0,
          -1.4,
          0
        ],
        "scale": [
          0.625,
          0.625,
          0.625
        ]
      },
      "fixed": {
        "rotation": [
          0,
          0,
          0
        ],
        "translation": [
          0,
          0,
          0
        ],
        "scale": [
          0.5,
          0.5,
          0.5
        ]
      }
    }
  }`)

  fs.writeFileSync(path.join(basePath, "models", "item", `${wood}_taburet.json`), `{
    "parent": "betterend:block/${wood}_taburet"
  }`)
})

console.log()
for (let i=0; i<woods.length; i++){
    let wood = woods[i]
    
    console.log(`"block.betterend.${wood}_bar_stool": "${woodEN[i]} Bar Stool",`)
    console.log(`"block.betterend.${wood}_chair": "${woodEN[i]} Chair",`)
    console.log(`"block.betterend.${wood}_taburet": "${woodEN[i]} Stool",`)
}

console.log()
for (let i=0; i<woods.length; i++){
    let wood = woods[i]
    
    console.log(`"block.betterend.${wood}_bar_stool": "${woodDE[i]} Barhocker",`)
    console.log(`"block.betterend.${wood}_chair": "${woodDE[i]} Stuhl",`)
    console.log(`"block.betterend.${wood}_taburet": "${woodDE[i]} Hocker",`)
}