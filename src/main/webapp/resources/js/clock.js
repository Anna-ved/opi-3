let hrs = document.getElementById("hrs");
let min = document.getElementById("min");
let sec = document.getElementById("sec");
let dayname = document.getElementById("dayname");
let month = document.getElementById("month");
let daynum = document.getElementById("daynum");
let year = document.getElementById("year");

const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

function updateClock(){
    let currentTime = new Date();

    hrs.innerHTML = currentTime.getHours();
    min.innerHTML = currentTime.getMinutes();
    sec.innerHTML = currentTime.getSeconds();
    dayname.innerHTML = days[currentTime.getDay()];
    month.innerHTML = months[currentTime.getMonth()];
    daynum.innerHTML = currentTime.getDate();
    year.innerHTML = currentTime.getFullYear();
}
updateClock();
setInterval(updateClock, 10000);


