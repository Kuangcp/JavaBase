/**
* @author       Richard Davey <rich@photonstorm.com>
* @copyright    2015 Photon Storm Ltd.
* @license      {@link http://choosealicense.com/licenses/no-license/|No License}
* 
* @description  This example requires the Phaser Virtual Joystick Plugin to run.
*               For more details please see http://phaser.io/shop/plugins/virtualjoystick
*/

var game = new Phaser.Game(1190, 590, Phaser.AUTO, 'phaser-example');

function connected() {
    let ws = new WebSocket("ws://192.168.1.102:7094/ws?userId=120");
    ws.onopen = function () {
        console.log('connected');
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

//   toggleFullScreen()

var PhaserGame = function () {

    this.background = null;
    this.player = null;
    this.speed = 300;

    this.weapon1;
    this.weapon2;
    this.weapon3;

    this.pad;

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

        this.stick = this.pad.addStick(150, 430, 100, 'arcade');
        this.stick.deadZone = 0;
        this.stick.scale = 0.85;

        this.buttonA = this.pad.addButton(650, 420, 'arcade', 'button1-up', 'button1-down');
        this.buttonA.onDown.add(this.fireBullet, this);
        this.buttonA.scale = 0.9;
        this.buttonA.repeatRate = 100;
        this.buttonA.addKey(Phaser.Keyboard.CONTROL);

        this.buttonB = this.pad.addButton(790, 320, 'arcade', 'button2-up', 'button2-down');
        this.buttonB.onDown.add(this.fireRocket, this);
        this.buttonB.scale = 0.9;
        this.buttonB.repeatRate = 500;
        this.buttonB.addKey(Phaser.Keyboard.Z);

        this.buttonC = this.pad.addButton(920, 420, 'arcade', 'button3-up', 'button3-down');
        this.buttonC.onDown.add(this.fireSpreadShot, this);
        this.buttonC.scale = 0.9;
        this.buttonC.addKey(Phaser.Keyboard.SPACEBAR);
    },

    fireBullet: function () {
        // toggleFullScreen()
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
            ws.send(JSON.stringify(x))
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

            ws.send(JSON.stringify(key))
    }

};

game.state.add('Game', PhaserGame, true);
