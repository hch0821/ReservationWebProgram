var curStart = 0
var curCategoryId = 0
var flag = true
const NUM_ITEM = 4

function getPromotions() {
    var oReq = new XMLHttpRequest()
    oReq.addEventListener("load", function () {
        var jsonobj = JSON.parse(this.responseText)
        data = jsonobj.items

        // console.log(data)
        var html = document.querySelector("#promotionItem").innerHTML
        var resultHTML = ""
        var visual_img = document.querySelector(".visual_img")
        data.forEach((v) => {
            resultHTML += html.replace("{productImageUrl}", v.productImageUrl)
            visual_img.innerHTML = resultHTML
        })

        animatePromotions(visual_img)
    })
    oReq.open("GET", "http://localhost:8080/reserv/api/promotions")
    oReq.send()
}

function animatePromotions(visual_img) {
    console.log(visual_img)
    var visual_img = document.querySelector(".visual_img")
    var promotion_len = visual_img.childElementCount
    var children = visual_img.children
    var idx = 0;
    for (var i = 0; i < children.length; i++) {
        children[i].style.left = "100%"
        children[i].style.display = ""
    }
    children[0].style.left = 0


    const interval = setInterval(() => {
        children[idx].style.left = "100%"
        children[idx].style.display = "none"
        idx = (idx + 1) % promotion_len
        children[idx].style.display = ""
        children[idx].style.left = "0"
        if (idx == promotion_len - 1) {
            for (var i = 0; i < children.length; i++) {
                children[i].style.left = "100%"
                children[i].style.display = ""
            }
            children[0].style.left = 0
        }
    }, 3000);
}

function getCategories() {
    var oReq = new XMLHttpRequest()
    oReq.addEventListener("load", function () {
        var jsonobj = JSON.parse(this.responseText)
        data = jsonobj.items

        var html = document.querySelector("#tabitem").innerHTML
        var resultHTML = ""
        var tab_list = document.querySelector(".event_tab_lst.tab_lst_min")
        data.forEach((v) => {
            resultHTML += html.replace("{name}", v.name)
                .replace(/{id}/g, v.id)
            tab_list.innerHTML = resultHTML
        })
        getProducts(true, 0, 0)
    })

    oReq.open("GET", "http://localhost:8080/reserv/api/categories")
    oReq.send()
}

function getProducts(isTabClicked, categoryId, start) {
    // /reserv/api/products?categoryId={id}&start=0
    // console.log('getProducts!!')
    var oReq = new XMLHttpRequest()
    oReq.addEventListener("load", function () {
        var jsonobj = JSON.parse(this.responseText)
        // console.log(jsonobj.items)
        // console.log(jsonobj.totalCount)
        var item = jsonobj.items;


        if (isTabClicked) {
            emptyProducts()
            document.querySelector(".event_lst_txt>.pink").innerHTML = jsonobj.totalCount + "개"
            var children = document.querySelector(".event_tab_lst.tab_lst_min").children
            for (var i = 0; i < children.length; i++) {
                children[i].firstElementChild.className = "anchor"
            }
            children[categoryId].firstElementChild.className = "anchor active"
        }
        getProductInfo(item)
    })
    curStart = start;
    curCategoryId = categoryId;
    // console.log('categoryId: ' + categoryId)
    oReq.open("GET", "http://localhost:8080/reserv/api/products?categoryId=" + categoryId + "&start=" + start)
    oReq.send()
}

function emptyProducts() {
    var event_boxes = document.querySelector(".wrap_event_box").children
    event_boxes[0].innerHTML = ""
    event_boxes[1].innerHTML = ""
    curStart = 0
}

function getProductInfo(data) {
    // console.log(data)
    var html = document.querySelector("#itemList").innerHTML
    var event_boxes = document.querySelector(".wrap_event_box").children

    var length = 0
    data.forEach((v) => {
        // console.log(v)
        var resultHTML = ""
        resultHTML = html.replace("{productId}", v.productId)
            .replace(/{productDescription}/g, v.productDescription)
            .replace("{productContent}", v.productContent)
            .replace("{placeName}", v.placeName)
            .replace("{productId}", v.productId)
        if (flag) {
            event_boxes[0].innerHTML += resultHTML
        }
        else {
            event_boxes[1].innerHTML += resultHTML
        }
        flag = !flag
        length++
    })

    // console.log('data.length: ' + data.length)
    // console.log('NUM_ITEM: ' + NUM_ITEM)
    if (length < NUM_ITEM) {
        setVisibility(".more", false)
    }
    else {
        setVisibility(".more", true)
    }
}

function moreClicked() {
    getProducts(false, curCategoryId, curStart + NUM_ITEM)
}

function setVisibility(selectors, isShown) {
    // console.log('setVisibility: ' + isShown)
    document.querySelector(selectors).style.display = isShown ? "inline" : "none";
}

window.addEventListener('load', function () {
    getCategories()
    getPromotions()
})

