const ROWS = 6;
const COLS = 7;
let circles = [];
let player;
let webSocket = null;
//when waiting for server ServerStatus is false
let ServerStatus = true;
//At the end of the game the endgame is true
let endgame = false;
let TopCell =[];

function preload()


{    
    var address = "ws://" + document.domain + ":" + location.port + "/ConnectFourWeb/endpoint";

    webSocket = new WebSocket(address);

    webSocket.onopen = function (event)
    {
        
        let col = color(255,255,255); //use color instead of fill
        button = createButton('Restart');
        button.id("RestartBT");
        button.style('font-size', '30px');
        button.style('background-color', col);
        button.position(900,650);
        button.mousePressed(Restart);
        alert("connection established");
    };
    webSocket.onclose = function (event)
    {
        alert("connection closed");
    };
    webSocket.onmessage = function (event)
    {
        // Message event.data
        let data = event.data 
        if(data[0] == 'Y' || data[0] == 'T'){
            endgame =true;
            alert(data);
            setTimeout(alert("To restart click the restart button"), 1000);
        }
        else
        {
             let col = data[0];
             let row = data[1];
             circles[row][col].setState('yellow');
             TopCell[col]=TopCell[col]-1;
             ServerStatus =true;
        }
       
    };
}
function setup() {
    endgame = false;
    ServerStatus = true;
    button.mousePressed(Restart);
    createCanvas(700, 600);
    player = 'red';
   
    for (let i = 0; i < ROWS; i++) {
        circles[i] = [];
        for (let j = 0; j < COLS; j++) {
            let sqrW = 700 / COLS;
            let sqrH = 600 / ROWS;
            let r = 40;
            let x = (j + 1) * sqrW - sqrW / 2;
            let y = (i + 1) * sqrH - sqrH / 2;
            circles[i][j] = new circleObj(x, y, r);
            TopCell[j] = 5;
        }
    }
   
   
}


function Restart()
{
    if(ServerStatus == true || endgame == true){
        webSocket.send(-1);
        setup();
    }
    else
    {
        alert("Please wait,We are Waiting for server");
    }
}


function draw() {
    //background(0, 0, 125);
    background(0, 128, 0);
    for (let i = 0; i < ROWS; i++) {
        for (let j = 0; j < COLS; j++) {
            circles[i][j].draw();
        }
    }
}

function mousePressed() {
if(ServerStatus == true && endgame == false){
    for (let i = 0; i < ROWS; i++) {
        for (let j = 0; j < COLS; j++) {
            if (circles[i][j].touched()) {
                if (circles[TopCell[j]][j].state == "white") {
                    ServerStatus = false;
                    circles[TopCell[j]][j].setState(player);
                    //alert(TopCell[j]);
                    TopCell[j]=TopCell[j]-1;
                    //let the server know
                    if(webSocket!==null)
                        webSocket.send(j);

                }


            }
        }
    }
}
}
class circleObj {
    constructor(x, y, r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.state = 'white';
    }

    draw() {
        switch (this.state) {
            case 'white':
                fill(255);
                break;
            case 'red':
                fill(255, 0, 0);
                break;
            case 'yellow':
                fill(255, 255, 0);
                break;
        }
        ellipse(this.x, this.y, this.r * 2, this.r * 2);
    }
    setState(state) {
        this.state = state;
    }
    touched() {
        let d = dist(this.x, this.y, mouseX, mouseY);
        return d <= this.r;
    }
}