function Page(pageUnit, pageSize, totalCount, currentPage) {
	this.pageUnit = Math.floor(pageUnit);
	this.pageSize = Math.floor(pageSize);
	this.totalCount = totalCount;
	this.maxPage = (pageUnit == 0 ? totalCount : Math.floor((totalCount-1) / pageUnit) + 1);
	this.currentPage = currentPage > this.maxPage ? this.maxPage : Math.floor(currentPage);
	this.beginUnitPage = (Math.floor((currentPage - 1) / pageSize)) * pageSize + 1;
	this.endUnitPage = (this.beginUnitPage > pageSize ? this.beginUnitPage : pageSize) >= this.maxPage ? this.maxPage : this.maxPage < this.beginUnitPage + (pageSize - 1) ? this.maxPage : this.beginUnitPage + (pageSize - 1);
}
Page.prototype.hasNextPage = function() {
			return this.currentPage < this.maxPage;
		};
Page.prototype.hasPreviousPage = function() {
	return this.currentPage > 1;
};
Page.prototype.getNextPage = function() {
	return this.currentPage + 1;
};
Page.prototype.getPreviousPage = function() {
	return this.currentPage - 1;
};
Page.prototype.hasNextPageUnit = function() {
	return this.endUnitPage < this.maxPage;
};
Page.prototype.hasPreviousPageUnit = function() {
	return this.currentPage >= this.pageUnit + 1;
};
Page.prototype.getStartOfNextPageUnit = function() {
	return (this.endUnitPage + 1 < this.maxPage) ? this.endUnitPage + 1 : this.maxPage; 
};
Page.prototype.getStartOfPreviousPageUnit = function() {
	return (this.beginUnitPage - 1 > 1) ? this.beginUnitPage - 1 : 1;
};
Page.prototype.getPageOfNextPageUnit = function() {
	return (this.currentPage + this.pageUnit < this.maxPage)
    ? this.currentPage + this.pageUnit : this.maxPage;
};
Page.prototype.getPageOfPreviousPageUnit = function() {
	return (this.currentPage - this.pageUnit > 1) ? this.currentPage - this.pageUnit : 1;
};
Page.prototype.getEndListPage = function() {
	return this.currentPage;
};


(function($) {
	    
	$.fn.quickPager = function(options) {
	
		var defaults = {
			pageIndexId: "pageIndex",
			searchButtonId: "searchButton",
			searchUrl: "#",
            pageSize: 10,
            currentPage: 1,
            formId: "",
            callback: null,
            param: {},
			holder: ""
    	};
    	var options = $.extend(defaults, options);
	  	
		//leave this
		var selector = $(this);
		var page = new Page(options.pageUnit, options.pageSize, options.totalCount, options.currentPage);
/*
		alert(" pageUnit = " + page.pageUnit +
		      "\n pageSize = " + page.pageSize +
		      "\n totalCount = " + page.totalCount +
		      "\n maxPage = " + page.maxPage +
		      "\n currentPage = " + page.currentPage + 
		      "\n beginUnitPage " + page.beginUnitPage + 
		      "\n endUnitPage " + page.endUnitPage);
		
*/		// remove child nodes
		selector.children().remove();
		selector.append('<nav aria-label="..." style="text-align:center"><ul class="pagination">')
		// draw anchor tags
//		if(page.maxPage > page.pageSize) {
//			selector.append('<a href="'+ options.searchUrl +'" class="direction prev" value="End"><span> </span> <span> </span> 처음</a>');
//		}
		if(page.maxPage > page.pageSize) {
			selector.append('<li class="page-item"><a class="page-link direction prev" href="'+ options.searchUrl +'" tabindex="-1">이전</a></li>');
		}
		for(i=page.beginUnitPage; i<=page.endUnitPage; i++) {
			if (i==page.currentPage) {
				selector.append('<li class="page-item active"><a class="page-link" href="#">'+i+'<span class="sr-only">(current)</span></a></li>');
			} else {
				selector.append('<li class="page-item"><a class="page-link" href="'+ options.searchUrl +'">'+i+'</a></li>');
			}
		}
		if(page.maxPage > page.pageSize) {
			selector.append('<li class="page-item"><a class="page-link direction next" href="'+ options.searchUrl +'"> 다음</a></li>');
		}
//		if(page.maxPage > page.pageSize) {
//			selector.append('<a href="'+ options.searchUrl +'" class="direction next" value="End">끝 <span> </span> <span> </span> </a>');
//		}
		selector.append('</ul></nav>')

		// add click event handler & calculate appropriate pageIndex
		// make sure that setting options about searchButton & pageIndex id
		selector.find("a").each(function(i) {
			$(this).click( function() {
				if($(this).hasClass("direction")) {
					if($(this).hasClass("next")) {
						if($(this).attr('value')== "End") {							
							$("#"+options.pageIndexId).val(page.maxPage);
						} else {							
							$("#"+options.pageIndexId).val(page.getStartOfNextPageUnit());
						}

					} else {
						if($(this).attr('value')== "End") {
							$("#"+options.pageIndexId).val(1);
						} else {
							$("#"+options.pageIndexId).val(page.getStartOfPreviousPageUnit());
						}
					}
				} else {
					$("#"+options.pageIndexId).val($(this).text());
				}
				if(options.callback){
					options.callback(options.param);
				}
				else if(options.formId != ""){
					$("#"+options.formId).trigger("submit");
				}
				else{
					$("#"+options.searchButtonId).trigger("click");
				}
			});
		});
			  
	};

	$( document ).ready(function() {
		$('#cssmenu li.has-sub>a').on('click', function(){
				$(this).removeAttr('href');
				var element = $(this).parent('li');
				if (element.hasClass('open')) {
					element.removeClass('open');
					element.find('li').removeClass('open');
					element.find('ul').slideUp();
				}
				else {
					element.addClass('open');
					element.children('ul').slideDown();
					element.siblings('li').children('ul').slideUp();
					element.siblings('li').removeClass('open');
					element.siblings('li').find('li').removeClass('open');
					element.siblings('li').find('ul').slideUp();
				}
			});

			$('#cssmenu>ul>li.has-sub>a').append('<span class="holder"></span>');

			(function getColor() {
				var r, g, b;
				var textColor = $('#cssmenu').css('color');
				textColor = textColor.slice(4);
				r = textColor.slice(0, textColor.indexOf(','));
				textColor = textColor.slice(textColor.indexOf(' ') + 1);
				g = textColor.slice(0, textColor.indexOf(','));
				textColor = textColor.slice(textColor.indexOf(' ') + 1);
				b = textColor.slice(0, textColor.indexOf(')'));
				var l = rgbToHsl(r, g, b);
				if (l > 0.7) {
					$('#cssmenu>ul>li>a').css('text-shadow', '0 1px 1px rgba(0, 0, 0, .35)');
					$('#cssmenu>ul>li>a>span').css('border-color', 'rgba(0, 0, 0, .35)');
				}
				else
				{
					$('#cssmenu>ul>li>a').css('text-shadow', '0 1px 0 rgba(255, 255, 255, .35)');
					$('#cssmenu>ul>li>a>span').css('border-color', 'rgba(255, 255, 255, .35)');
				}
			})();

			function rgbToHsl(r, g, b) {
			    r /= 255, g /= 255, b /= 255;
			    var max = Math.max(r, g, b), min = Math.min(r, g, b);
			    var h, s, l = (max + min) / 2;

			    if(max == min){
			        h = s = 0;
			    }
			    else {
			        var d = max - min;
			        s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
			        switch(max){
			            case r: h = (g - b) / d + (g < b ? 6 : 0); break;
			            case g: h = (b - r) / d + 2; break;
			            case b: h = (r - g) / d + 4; break;
			        }
			        h /= 6;
			    }
			    return l;
			}
		});
})(jQuery);

