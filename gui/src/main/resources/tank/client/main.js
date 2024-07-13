/**
* @author       Richard Davey <rich@photonstorm.com>
* @copyright    2015 Photon Storm Ltd.
* @license      {@link http://choosealicense.com/licenses/no-license/|No License}
* 
* @description  This example requires the Phaser Virtual Joystick Plugin to run.
*               For more details please see http://phaser.io/shop/plugins/virtualjoystick
*/

let borderW, borderH;
let directX, directY;
let aX, aY;
let bX, bY;
let cX, cY;

// console.log(screen.height);
// alert(screen.height + ' ' + screen.width)
let phone = screen.width < 900;
if (phone) {
    borderW = screen.width;
    borderH = screen.height;

    directX = 170;
    directY = 250;
    directScale = 0.7;

    skillScale = 0.8;
    aX = 600;
    aY = 270;
    bX = 690;
    bY = 170;
    cX = 760;
    cY = 270;
} else {
    borderW = 1190;
    borderH = 590;

    directX = 150;
    directY = 430;
    directScale = 0.85;

    skillScale = 0.9;
    aX = 660;
    aY = 420;
    bX = 800;
    bY = 320;
    cX = 920;
    cY = 420;
}

var game = new Phaser.Game(borderW, borderH, Phaser.AUTO, 'phaser-example');

var wsUsable =false;
function connected() {
    try {
        wsUsable = false;
        let ws = new WebSocket("ws://192.168.1.104:7094/ws?uid=120");
        ws.onopen = function () {
            console.log('connected');
            // 等 open 函数回调后才可以调用 send，否则会报错 no longer object
            wsUsable = true;
        };
        ws.onmessage = function (evt) {
            console.log(evt.data)
        };
        ws.onclose = function (evt) {
            console.log("error");
        };
        ws.onerror = function (evt) {
            console.log("error");
        };

        return ws;
    } catch (e) {
        console.log(e);
        return null;
    }
}
var ws = connected();

function toggleFullScreen() {
    var docElm = document.documentElement;
    if (docElm.requestFullscreen) {
        docElm.requestFullscreen();
    } else if (docElm.msRequestFullscreen) {
        docElm.msRequestFullscreen();
    } else if (docElm.mozRequestFullScreen) {
        docElm.mozRequestFullScreen();
    } else if (docElm.webkitRequestFullScreen) {
        docElm.webkitRequestFullScreen();
    }
}

var PhaserGame = function () {

    this.background = null;
    this.player = null;

    this.pad;
    this.speed = 300;

    this.stick;

    this.buttonA;
    this.buttonB;
    this.buttonC;

};

PhaserGame.prototype = {

    init: function () {
        this.game.renderer.renderSession.roundPixels = true;
        this.physics.startSystem(Phaser.Physics.ARCADE);

    },

    preload: function () {
        this.load.atlas('arcade', 'assets/arcade-joystick.png', 'assets/arcade-joystick.json');
        this.load.image('background', 'assets/back.png');
        this.load.image('player', 'assets/dark.png');
        this.load.image('bullet2', 'assets/bullet2.png');
        this.load.image('bullet9', 'assets/bullet9.png');
        this.load.image('bullet10', 'assets/bullet10.png');

    },

    create: function () {

        // this.background = this.add.tileSprite(0, 0, this.game.width, this.game.height, 'background');
        // this.background.autoScroll(-40, 0);

        this.player = this.add.sprite(64, 200, 'player');

        this.physics.arcade.enable(this.player);

        this.player.body.collideWorldBounds = true;

        this.pad = this.game.plugins.add(Phaser.VirtualJoystick);

        this.stick = this.pad.addStick(directX, directY, 100, 'arcade');
        this.stick.deadZone = 0;
        this.stick.scale = directScale;

        this.buttonA = this.pad.addButton(aX, aY, 'arcade', 'button1-up', 'button1-down');
        this.buttonA.onDown.add(this.fireBullet, this);
        this.buttonA.scale = skillScale;
        this.buttonA.repeatRate = 1000;
        this.buttonA.addKey(Phaser.Keyboard.CONTROL);

        this.buttonB = this.pad.addButton(bX, bY, 'arcade', 'button2-up', 'button2-down');
        this.buttonB.onDown.add(this.fireRocket, this);
        this.buttonB.scale = skillScale;
        this.buttonB.repeatRate = 300;
        this.buttonB.addKey(Phaser.Keyboard.Z);

        this.buttonC = this.pad.addButton(cX, cY, 'arcade', 'button3-up', 'button3-down');
        this.buttonC.onDown.add(this.fireSpreadShot, this);
        this.buttonC.scale = skillScale;
        this.buttonB.repeatRate = 300;
        this.buttonC.addKey(Phaser.Keyboard.SPACEBAR);
    },

    fireBullet: function () {
        toggleFullScreen();

        // ws = connected();
        // this.weapon1.fire(this.player);

    },

    fireRocket: function () {

        // this.weapon2.fire(this.player);

    },

    fireSpreadShot: function () {

        // this.weapon3.fire(this.player);
        let x = {
            s: true
        }
        if(wsUsable){
            ws.send(JSON.stringify(x))
        }
    },

    update: function () {

        let key = {
            d: -1
        }
        if (this.stick.isDown) {
            // console.log(this.stick.rotation, this.stick.force);

            // this.physics.arcade.velocityFromRotation(this.stick.rotation, this.stick.force * maxSpeed, this.player.body.velocity);

            if (this.stick.rotation > -2.2 && this.stick.rotation < -0.85) {
                key.d = 0
            }
            else if (this.stick.rotation > 0.85 && this.stick.rotation < 2.2) {
                key.d = 1
            }
            else if (this.stick.rotation <= -2.2 || this.stick.rotation >= 2.2) {
                key.d = 2
            }
            else if (this.stick.rotation >= -0.85 && this.stick.rotation <= 0.85) {
                key.d = 3
            }
            // console.log(key)
        }
        else {
            // this.player.body.velocity.set(0);
        }
        if (wsUsable) {
            ws.send(JSON.stringify(key))
        }
    }

};

game.state.add('Game', PhaserGame, true);
