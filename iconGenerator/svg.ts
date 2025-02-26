// deno run --allow-read --allow-write svg.ts

import TextToSVG from "npm:text-to-svg"

const iconDirUrl = new URL("./icon", import.meta.url);
const iconDir = iconDirUrl.pathname;
Deno.mkdir(iconDir, {recursive: true});

console.log("save to")
console.log(iconDir);

const textToSVG = TextToSVG.loadSync();
const attributes = {fill: 'black', width: '24', height: '24', viewBox: '0 0 24 24'};
const options = {x: 12, y: 12, fontSize: 24, anchor: 'center middle', attributes: attributes};

for (let i = 1; i < 32; i++) {
    const num = i.toString().padStart(2, "0");
    const url = new URL(`icon/notification_icon_${num}.svg`, iconDirUrl);
    const svg = textToSVG.getSVG(num, options);
    Deno.writeTextFile(url, svg, {create: true, append: false});
}

console.log("done");
