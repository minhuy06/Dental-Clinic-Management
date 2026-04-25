document.addEventListener('DOMContentLoaded',function(){
var track=document.getElementById('slideshowTrack'),dotsC=document.getElementById('slideDots');
if(!track)return;
var origSlides=track.querySelectorAll('.slide'),total=origSlides.length;
if(total===0)return;

var lastClone=origSlides[total-1].cloneNode(true);lastClone.classList.add('clone');track.insertBefore(lastClone,track.firstChild);
var firstClone=origSlides[0].cloneNode(true);firstClone.classList.add('clone');track.appendChild(firstClone);
var cur=1,transitioning=false,autoInt;
track.style.transition='none';track.style.transform='translateX(-'+(cur*100)+'%)';

if(dotsC&&total>0){for(var i=0;i<total;i++){var d=document.createElement('button');d.className='slide-dot'+(i===0?' active':'');d.setAttribute('data-index',i);d.addEventListener('click',function(){if(!transitioning){goTo(parseInt(this.getAttribute('data-index'))+1);}});dotsC.appendChild(d);}}

function goTo(idx){if(transitioning)return;transitioning=true;cur=idx;track.style.transition='transform 0.6s ease';track.style.transform='translateX(-'+(cur*100)+'%)';upDots();}
track.addEventListener('transitionend',function(){if(cur>=total+1){track.style.transition='none';cur=1;track.style.transform='translateX(-'+(cur*100)+'%)';}if(cur<=0){track.style.transition='none';cur=total;track.style.transform='translateX(-'+(cur*100)+'%)';}transitioning=false;upDots();});
function upDots(){if(!dotsC)return;var r=cur-1;if(r<0)r=total-1;if(r>=total)r=0;dotsC.querySelectorAll('.slide-dot').forEach(function(d,i){d.classList.toggle('active',i===r);});}
window.changeSlide=function(dir){if(!transitioning){goTo(cur+dir);resetAuto();}};
function startAuto(){clearInterval(autoInt);autoInt=setInterval(function(){goTo(cur+1);},3000);}
function resetAuto(){clearInterval(autoInt);startAuto();}
startAuto();

var ss=document.getElementById('slideshow');
if(ss){ss.addEventListener('mouseenter',function(){clearInterval(autoInt);});ss.addEventListener('mouseleave',function(){startAuto();});}

// MOUSE DRAG + TOUCH SWIPE
var startX=0,isDragging=false;
if(ss){
ss.addEventListener('mousedown',function(e){isDragging=true;startX=e.clientX;track.style.transition='none';});
ss.addEventListener('mousemove',function(e){if(!isDragging)return;var diff=e.clientX-startX;var offset=-(cur*ss.offsetWidth)+diff;track.style.transform='translateX('+offset+'px)';});
ss.addEventListener('mouseup',function(e){if(!isDragging)return;isDragging=false;var diff=e.clientX-startX;if(Math.abs(diff)>50){if(diff<0)goTo(cur+1);else goTo(cur-1);}else{track.style.transition='transform 0.6s ease';track.style.transform='translateX(-'+(cur*100)+'%)';}resetAuto();});
ss.addEventListener('mouseleave',function(){if(isDragging){isDragging=false;track.style.transition='transform 0.6s ease';track.style.transform='translateX(-'+(cur*100)+'%)';}});

ss.addEventListener('touchstart',function(e){startX=e.touches[0].clientX;track.style.transition='none';},{passive:true});
ss.addEventListener('touchmove',function(e){var diff=e.touches[0].clientX-startX;var offset=-(cur*ss.offsetWidth)+diff;track.style.transform='translateX('+offset+'px)';},{passive:true});
ss.addEventListener('touchend',function(e){var diff=e.changedTouches[0].clientX-startX;if(Math.abs(diff)>50){if(diff<0)goTo(cur+1);else goTo(cur-1);}else{track.style.transition='transform 0.6s ease';track.style.transform='translateX(-'+(cur*100)+'%)';}resetAuto();});
}

// FADE IN
var fades=document.querySelectorAll('.fade-in');
if('IntersectionObserver' in window){var obs=new IntersectionObserver(function(ents){ents.forEach(function(en){if(en.isIntersecting){en.target.classList.add('visible');obs.unobserve(en.target);}});},{threshold:0.15,rootMargin:'0px 0px -50px 0px'});fades.forEach(function(el){obs.observe(el);});}else{fades.forEach(function(el){el.classList.add('visible');});}

// SMOOTH SCROLL
document.querySelectorAll('a[href^="#"]').forEach(function(a){a.addEventListener('click',function(e){var id=this.getAttribute('href');if(id==='#')return;var t=document.querySelector(id);if(t){e.preventDefault();t.scrollIntoView({behavior:'smooth',block:'start'});var nm=document.getElementById('navMenu');if(nm)nm.classList.remove('open');}});});
});
