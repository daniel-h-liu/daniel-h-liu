const speech = new p5.SpeechRec('en-US', parseResult)
speech.continuous = true
speech.interimResults = false

let count
function setup(){


  createCanvas(windowWidth,windowHeight)
  background(255)
  fill(25)

  textSize(48)
  textAlign(CENTER)
  textStyle(BOLDITALIC)
  textFont('"Avenir Next", system-ui, sans-serif')
  text("Voice-Activated Pokédex", width/2, height/2 - 300)
  speech.start()

  text('Say a Pokémon', width/2, height/2)
}

function draw(){

}

function parseResult(){
  if(speech.resultValue){
    const pokemon = speech.resultString
      .split(' ')
      .pop();
    text(pokemon, width/2, height/2);
    background(255);
    
 

    var request = new XMLHttpRequest()
 
    request.open(
      'GET', 
      'https://pokeapi.co/api/v2/pokemon/' + pokemon.toLowerCase()
    )
    request.responseType = 'json'
    request.send()
    request.onload = function(){
      var data = this.response
      
      if(request.status >= 200 && request.status < 400){
        var type = ''
        for(var i = 0; i < data.types.length; i++){
          type += data.types[i].type.name + ' '
        }
        var id = data.id
        var url = data.sprites.front_default
        var back = data.sprites.back_default
        var moves = ''
        for(var i = 0; i < data.moves.length && i < 3; i++){
          moves += data.moves[i].move.name + ', '
        }

        display(pokemon, url, back, type, id, moves)
      }else{
        background(255)
        text('Error, try saying it clearer.', width/2, height/2)
        console.log(request.status)
      }
    }
    
  }

  function display(name, imageURL, back, type, id, moves){
    count = 1
    background(255)
    fill(0)
    loadImage(imageURL, loadImg)
    loadImage(back, loadImg)
    text(name, width/2, height/2)
    fill(120,120,120)
    text(' #' + id, width/2 + name.length * 20 + 20, height/2)
    text('Type: ' + type, width/2, height/2 + 100)
    
    moves = moves.substring(0, moves.length - 2)
    text('Common moves: ' + moves, width/2, height/2 + 200)
  }
  function loadImg(img){
    img.resize(300,300)
    imageMode(CENTER)
    image(img, width/2 + 200 * Math.pow(-1,count), height/2 - 200)
    count++
  }
}
