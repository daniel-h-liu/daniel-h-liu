let main_menu_bg
let sel
let startButton
let aboutButton
let mainButton

function preload(){
  main_menu_bg = loadImage('main_menu.jpg');

}

function setup(){
  createCanvas(windowWidth, windowHeight);
  

  textAlign(CENTER);
  fill(0);
  textFont("Inria-Serif");
  textSize(48);
  page = 0
  mainButton = createButton("").class("button").hide();
  mainButton.position(windowWidth/2, windowHeight/2 + 200).center('horizontal');
  mainButton.position(mainButton.position[0], windowHeight/2 + 100);
  mainSpan = createSpan("GO BACK");
  mainButton.child(mainSpan);
  aboutButton = createButton("").class("button");
  aboutSpan = createSpan("ABOUT");
  aboutButton.child(aboutSpan);
  aboutButton.position(windowWidth / 2, windowHeight / 2 + 25).center('horizontal');
  startButton = createButton("").class("button"); 
  startButton.position(windowWidth / 2, windowHeight / 2 + 100).center('horizontal');
  startSpan = createSpan("FIND PARK");
  startButton.child(startSpan);
  aboutButton.mouseClicked(aboutPage);
  startButton.mouseClicked(startPage);
  input = createInput().class("input");
  input.center();
  input.position(input.position[0], input.position[1]-50)
  input.attribute('placeholder','Address');
  submit = createButton('Submit').class("buttonA");
  submit.position(windowWidth/2+100, windowHeight/2);
  submit.mouseClicked(run);

  
  mainmenu();
}

function mainmenu(){
  input.hide();
  submit.hide();
  mainButton.hide();
  //boxDiv.hide();
  background(main_menu_bg);
  fill(255);
  text("Parks for Pandemics", windowWidth/2, windowHeight/2 - 100);
  fill(0);
  aboutButton.show();
  startButton.show();

}

function aboutPage(){
  startButton.hide();
  aboutButton.hide();
  background(255);
  textFont("Inria-Serif");
  textSize(24);
  text("Welcome to Parks for Pandemics!", windowWidth/2, windowHeight/2 - 120);
  textSize(14);
  text("The pandemic has led to sudden lockdowns that many were not ready for. This put \npeople in a situation where they do not get to go outside as often as they should. \nTherefore, we would like to encourage people to go outside, get fresh air, and see some sunshine \nthrough this program that allows people to discover less population-dense areas, such as \nnatural parks, near them. \n\nType in an address, and you should be able to find \ areas for you to walk nearby -- away from \nthe masses. Nonetheless, please be mindful to social distance and wear a mask!", windowWidth/2, windowHeight/2 - 75);
  textSize(48);
  mainButton.show();
  mainButton.mouseClicked(mainmenu);
}

function startPage(){
  
  background(main_menu_bg);
  startButton.hide();
  aboutButton.hide();
  input.show();
  submit.show();

  
  //window.document.getElementsByTagName("body").child(startPage);
  mainButton.show();
  
  //mainButton.mouseClicked(mainback);
  mainButton.mouseClicked(mainmenu);
  
}


var pois = []
function run(){
  let lat;
  let lon;
  input.hide();
  submit.hide();
  mainButton.hide();
  //background(255);
  var place = encodeURIComponent(input.value());


  
  var laloRequest = new XMLHttpRequest();
 
    laloRequest.open(
      'GET', 
      'https://api.opencagedata.com/geocode/v1/json?q=' + place + '&key=9e361d90417240ba8ed5412b7d98f824&pretty=1'
    );
    laloRequest.responseType = 'json';
    laloRequest.send();
    laloRequest.onload = function(){
      var data = this.response
      if(this.status >= 200 && this.status < 400){
        //var mapUrl = data.results[0].annotations.OSM.url;
        //console.log(mapUrl);
        lat = data.results[0].geometry.lat;
        lon = data.results[0].geometry.lng;
        
        console.log(lat);
        console.log(lon);

        var placesRequest = new XMLHttpRequest();
        var url = 'https://api.tomtom.com/search/2/nearbySearch/.json?lat='+lat+'&lon='+lon+'&idxSet=POI&categorySet=7360%2C9927003%2C9362%2C7302%2C7376004%2C9362036%2C9927003%2C7302005&openingHours=nextSevenDays&key=SdTzSmdYJlkRfuldyzkawPAHe1SEQqOK';
        console.log(url);
        placesRequest.open(
          'GET',
          url
        );

        placesRequest.send();
        placesRequest.onload = function(){
          var data = JSON.parse(this.response);
          if(this.status >= 200 && this.status < 400){
            console.log(data.summary.totalResults);
            var results = data.results;
            if(results == null){
              console.log("NO RESULTS!");
              startPage();
              return;
            }
            for(var i = 0; i < results.length; i++){
              pois.push(results[i]);
            }
            var name = data.results[0].poi.name;
            
            display(); 
          }else{
            background(255)
            text('Error, try again.', width/2, height/2)
            console.log('error')
          }
        }
      }else{
        background(255)
        text('Error, try again.', width/2, height/2)
        console.log('error')
      }
    }
    //laloRequest.abort();

    //open map link: https://www.openstreetmap.org/?mlat=40.81333&mlon=-73.62466#map=17/40.81333/-73.62466

    

    // campground - 7360; zoo/arbetorium/garden - 9927003; park/recreation - 9362; trails - 7302;Natural Attraction - 7376004; River - 9362036; zoo - 9927003; something trail -  7302005;
    //

 
}
let next
let prev
var pages = []
let page = 0

function display(){
  var maxPages = Math.floor(pois.length / 3);
  background(main_menu_bg);
  if(page >= pages.length - 1){
    createPage(pois);
  }
  if(pages.length > 0){
    pages[page].show();
  }
  prev = createButton("Previous").class("button");
  prev.mouseClicked(decreasePage);
  next = createButton("Next").class("button");
  next.mouseClicked(increasePage);
  if(page == maxPages - 1){
    next.hide();
    prev.show();
  }else if(page > 0){
    prev.show();
    next.show();
  }else if(page == 0){
    prev.hide();
    next.show();
  }
}

function createPage(){
  var list = createDiv("").class("screen");
  for(var i = 0; i < 3; i++){
    var poi = pois[page * 3 + i];
    //alert(typeof poi)
    //alert(typeof poi.poi.name)
    var div = createDiv(poi.poi.name).class("item").position(25,windowHeight/3*i + 25);
    var address = createSpan(poi.address.freeformAddress).class("desc");
    div.child(address);

    task(div, poi)
    
    list.child(div);
  }
  pages.push(list);

}

function decreasePage(){
  prev.hide();
  next.hide();
  pages[page].hide();
  page--;
  //console.log(page);
  display(pois);
}
function increasePage(){
  prev.hide();
  next.hide();
  console.log("page = " + page);
  console.log(pages);
  pages[page].hide();
  page++;
  //console.log(page);
  display(pois);
}

//object["property"] = value;
//https://sentry.io/answers/javascript-script-error/#:~:text=“Script%20error”%20is%20what%20browsers,%2C%20port%2C%20or%20protocol).&text=It%27s%20to%20avoid%20a%20script,that%20it%20doesn%27t%20control.

function task(div, poi){
  var detailsRequest = new XMLHttpRequest();
  //console.log(poi.dataSources.poiDetails[0]);
  try{
    var url = 'https://api.tomtom.com/search/2/poiDetails.json?key=SdTzSmdYJlkRfuldyzkawPAHe1SEQqOK&id=' + poi.dataSources.poiDetails[0].id;
    detailsRequest.open(
          'GET',
          url
        );
    //console.log(url);
    detailsRequest.send();
    detailsRequest.onload = function(){
      var data = JSON.parse(this.response);
      if(this.status >= 200 && this.status < 400){
        if(Object.keys(data.result).length == 0){
          div.child(createSpan("No More Information/Photo Available").class("desc"));
        }else{
          try{
            div.child(createSpan("Review: " + data.result.reviews[0].text).class("desc"));
            div.child(createSpan("Rating: " + data.result.rating.value + "/10").class("desc"));
          }catch(error){
            console.log('error');
          }
        }
      }
  
    }
    sleep(100);
  }catch(error){
    div.child(createSpan("No More Information/Photo Available").class("desc"));
  }
}

function sleep(milliseconds) {
  const date = Date.now();
  let currentDate = null;
  do {
    currentDate = Date.now();
  } while (currentDate - date < milliseconds);
}
