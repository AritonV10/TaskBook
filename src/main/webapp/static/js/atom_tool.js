$(function(){console.log("Atom_tool_tip loaded");document.addEventListener("mouseover",function(c){if(null!=c.target.getAttribute("aria-describedby")){c.preventDefault();var d=atom_tool_init(c.target.className.split(" "),c.target.dataset.atom,c.target.getAttribute("atom-action")),a=c.target,f=a.getBoundingClientRect(),b=a.getAttribute("atom-action")||"down",e=c.target.getAttribute("atom-top")||10;c=c.target.getAttribute("atom-bottom")||-15;d.style.top=("up"==b?f.top-a.offsetHeight:f.top+a.offsetHeight)+
"px";d.style.right=f.right+"px";d.style.left=f.left+a.offsetWidth/2-d.offsetWidth/2+"px";d.style.bottom=f.bottom+"px";d.style.visibility="visibile";d.style.opacity="1";d.style.transform="translateY("+("down"==b?e:c)+"px)";a.addEventListener("mouseleave",function(){d.style.opacity="0";atom_tool_dest()})}})});
function atom_tool_init(c,d,a){var f=document.querySelector("body"),b=document.createElement("div");a=a||"down";b.id="atom-tooltip";b.setAttribute("role","tooltip");b.setAttribute("atom-target",function(){var a="";c.forEach(function(b,d){a+=b+(d+1==c.length?"":" ")});return a}());var e=document.createElementNS("http://www.w3.org/2000/svg","svg"),h=document.createElementNS("http://www.w3.org/2000/svg","polygon"),g=document.createElement("div");g.className="atom-tooltip-2";g.append(d);b.appendChild(g);
h.setAttribute("points","down"==a?"30,4,4,60,60,60":"30,80,4,30,60,30");e.setAttribute("height","10");e.appendChild(h);b.insertBefore(e,g);f.appendChild(b);e.setAttribute("style","position: absolute; text-align: center; left: 0; right: 0; margin-right: auto; margin-left: auto; "+("down"===a?"top: -9px;":"bottom: -9px;"));e.setAttribute("width",b.offsetWidth);"down"!=a&&e.setAttribute("transform","rotate(360deg);");h.setAttribute("style","transform: translateX("+(b.offsetWidth-57.5)/2+"px) "+("down"!=
a?"translateY(-74px);":" ")+"; fill:"+window.getComputedStyle(b).getPropertyValue("background-color"));return b}function atom_tool_dest(){"undefined"!=typeof document.querySelector("#atom-tooltip")&&null!=document.querySelector("#atom-tooltip")&&document.querySelector("body").removeChild(document.querySelector("#atom-tooltip"))};