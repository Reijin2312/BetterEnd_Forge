const path = require("path")
const fs = require("fs")

const woods = ["pythadendron", "end_lotus", "lacugrove", "dragon_tree", "tenanea","helix_tree", "umbrella_tree", "jellyshroom", "lucernia", "mossy_glowshroom", "lucernia_jellyshroom"] 
const woodDE = ["Pythadendron", "Endlotus", "Lacugrove", "Drachenbaum", "Tenanea","Spiralbaum", "Schirmbaum", "Geleepilz", "Lucernia", "Glühmospilz", "Gelelucernia"] 
const woodEN = ["Pythadendron", "End Lotus", "Lacugrove", "Dragon Tree", "Tenanea","Helix Tree", "Umbrella Tree", "Jellyshroom", "Lucernia", "Mossy Glowshroom", "Jelly Lucernia"] 

const nameDE = "Hängendes {wood}schild"
const nameEN = "Hanging {wood} Sign"
woods.forEach(wood => {
    const blockName = wood+'_hanging_sign'
    const wallBlockName = wood+'_wall_hanging_sign'
    const basePath = "./src/main/resources/assets/betterend";
    const particleName = 'particles_'+wood;

    const blockState = `{
    "variants": {
        "": {
            "model": "betterend:block/${particleName}"
        }
    }
}`

    const particles = `{
    "textures": {
        "particle": "betterend:block/${wood}_planks"
    }
}`

    const model = `{
    "parent": "item/generated",
    "textures": {
        "layer0": "betterend:item/${blockName}"
    }
}`
    fs.writeFileSync(path.join(basePath, "blockstates", blockName+".json"), blockState);
    fs.writeFileSync(path.join(basePath, "blockstates", wallBlockName+".json"), blockState);
    fs.writeFileSync(path.join(basePath, "models", "block", particleName+".json"), particles);
    fs.writeFileSync(path.join(basePath, "models", "item", blockName+".json"), model);
})

console.log()
for (let i=0; i<woods.length; i++){
    let wood = woods[i]
    const blockName = wood+'_hanging_sign'
    console.log(`"block.betterend.${blockName}": "${nameEN.replace("{wood}", woodEN[i])}",`)
}

console.log()
for (let i=0; i<woods.length; i++){
    let wood = woods[i]
    const blockName = wood+'_hanging_sign'
    console.log(`"block.betterend.${blockName}": "${nameDE.replace("{wood}", woodDE[i])}",`)
}