
const API = "http://localhost:8080/api";

const qs = (s, r=document) => r.querySelector(s);
const qsa = (s, r=document) => [...r.querySelectorAll(s)];

function money(v){
  return new Intl.NumberFormat("en-IN",{style:"currency",currency:"INR",minimumFractionDigits:2,maximumFractionDigits:2}).format(Number(v)||0);
}
function num(v){return (Number(v)||0).toLocaleString("en-IN",{maximumFractionDigits:2});}
function esc(v){return String(v??"").replace(/[&<>"']/g,m=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"}[m]));}
function toast(message,type="success"){
  let el=qs("#toast"); if(!el)return;
  el.textContent=message; el.className=`toast show ${type}`;
  clearTimeout(window.__toast); window.__toast=setTimeout(()=>el.className="toast",3000);
}
async function api(path, options={}){
  const res=await fetch(API+path,{headers:{"Content-Type":"application/json",...(options.headers||{})},...options});
  const text=await res.text();
  let data=null; try{data=text?JSON.parse(text):null}catch{data=text}
  if(!res.ok){
    const message=data?.message || data?.error || (typeof data==="string"?data:`Request failed (${res.status})`);
    throw new Error(message);
  }
  return data;
}
function setConnection(ok){
  const t=qs("#connectionText"),d=qs("#statusDot");
  if(t)t.textContent=ok?"Backend Connected":"Backend Offline";
  if(d){d.style.background=ok?"var(--green)":"var(--red)";d.style.color=ok?"var(--green)":"var(--red)";}
}
function openModal(id){qs("#"+id)?.classList.add("show")}
function closeModal(id){qs("#"+id)?.classList.remove("show")}
document.addEventListener("click",e=>{
  const close=e.target.closest("[data-close]");
  if(close)closeModal(close.dataset.close);
});
