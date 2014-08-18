/*global $, jsRoutes, FormData */

$(function () {
    'use strict';

    function deleteFile(fileID) {
        var route = jsRoutes.controllers.Application.deleteFile();

        $.ajax({
            url: route.url,
            type: route.type,
            success: function (res) {},
            data: JSON.stringify({
                "fileID": fileID
            }),
            contentType: 'application/json'
        });
    }

    function progressHandlingFunction(e) {
        var pc = 100 - parseInt(100 - (e.loaded / e.total * 100), 10);
        $("#uploadProgress").attr('value', pc);
        $(".progress-percentage").attr('data-value', pc);
        $(".progress-percentage").css({
            "width": pc + '%'
        });
    }

    function generateNumberFromString(str, max) {
        var i, sum = 0;
        for (i = 0; i < str.length; i += 1) {
            sum += str.charCodeAt(i);
        }
        return sum % max;
    }

    function generateImageNameFromString(str) {
        var imageNumber;
        imageNumber = generateNumberFromString(str, 14);
        return 'assets/images/icons/svg/' + imageNumber + '.svg';
    }
    
    function generateFileCountText(count) {
        if (count < 2) {
            return '';   
        } else {
            return "(" + count + " files)";
        }
    }

    function generateUploadedFileBlock(file) {
        var fileSize, fileBlock;
        fileSize = parseFloat(Math.round(file.size / 1000000 * 100) / 100).toFixed(2) + " MB";

        if (fileSize === "0.00 MB") {
            fileSize = parseFloat(Math.round(file.size / 1000 * 100) / 100).toFixed(2) + " KB";
        }

        fileBlock = $('<div id="fileBox" value=' + file.id + '><div class="col-xs-4"><div class="tile uploadedfiletiles"><img src="' + generateImageNameFromString(file.id) + '" class="tile-image big-illustration"><h3 class="tile-title">' + file.name + '</h3><p>' + fileSize + ' ' + generateFileCountText(file.count) + '</p><p>' + file.id + '</p><input class="btn btn-block btn-lg btn-warning deleteFileButton" type="button" value="Delete"></div></div></div>');
        return fileBlock;
    }

    function generateServerFileBlock(file) {
        var fileSize, fileBlock;
        fileSize = parseFloat(Math.round(file.size / 1000000 * 100) / 100).toFixed(2) + " MB";

        if (fileSize === "0.00 MB") {
            fileSize = parseFloat(Math.round(file.size / 1000 * 100) / 100).toFixed(2) + " KB";
        }

        fileBlock = $('<div id="serverFileBox" value=' + file.name + '><div class="col-xs-4"><div class="tile uploadedfiletiles"><img src="' + generateImageNameFromString(file.name) + '" class="tile-image big-illustration"><h3 class="tile-title">' + file.name + '</h3><p>' + fileSize + '</p><p>' + file.id + '</p><input class="btn btn-block btn-lg btn-info selectFileButton" type="button" value="Select"></div></div></div>');
        return fileBlock;
    }

    function setUploadedFileDetails(file, fileBlockFunction, $filesDiv, $nofilesDiv) {
        var fileBlock = fileBlockFunction(file).hide();

        if ($nofilesDiv) {
            $nofilesDiv.remove();
        }
        $filesDiv.append(fileBlock);
        fileBlock.fadeIn();
    }

    function setUploadedFilesFromArray(files, fileBlockFunction, $filesDiv, $nofilesDiv) {
        var i;
        for (i = 0; i < files.length; i += 1) {
            setUploadedFileDetails(files[i], fileBlockFunction, $filesDiv, $nofilesDiv);
        }
    }

    function validateOrderIdField(str) {
        var result = $.isNumeric(str);

        if (!result) {
            $("#process-text-input").addClass("has-error");
            $("#process-text-input").find('span').remove();
        } else {
            $("#process-text-input").find('span').remove();
            $("#process-text-input").removeClass("has-error");
            $("#process-text-input").addClass("has-success");
            $("#process-text-input").append('<span class="input-icon fui-check-inverted"></span>');
        }

        return result;
    }

    function validateUploadField($fileField) {
        if ($fileField.val() === "") {
            $("#filepath-textbox").parent().addClass("has-error");
            $("#filepath-textbox").removeClass("login-field");
            return false;
        } else {
            $("#filepath-textbox").parent().removeClass("has-error");
            $("#filepath-textbox").addClass("login-field");
            return true;
        }
    }

    function regexToJavaRegex(str) {
        return str.split("\\").join("\\\\");
    }

    function fileExistsChecker() {
        if ($('#files').children().length < 1) {
            var noFilesDiv = $("<div id='no-files' class='text-center silver-grey collapsed'><h1>No files</h1></div>");
            $('#files').html(noFilesDiv);
            noFilesDiv.animate({
                height: '400px',
                opacity: 1
            }, {
                duration: 500
            });
        }
    }

    function highlightSearchText() {
        $("#searchQuery").highlightTextarea({
            words: ["\\([^()]*\\)"],
            color: "#cfe01b"
        });
    }

    function compareStrings(a, b) {
        // Assuming you want case-insensitive comparison
        a = a.toLowerCase();
        b = b.toLowerCase();

        return (a < b) ? -1 : (a > b) ? 1 : 0;
    }

    function setServerFiles() {
        var route = jsRoutes.controllers.Application.getAPIServerFiles();
        $.ajax({
            url: route.url,
            type: route.type,
            success: function (res) {
                res.files.sort(function (a, b) {
                    return compareStrings(a.name, b.name);
                });
                setUploadedFilesFromArray(res.files, generateServerFileBlock, $("#server-files"));
                //alert(JSON.stringify(res.files));
            }
        });
    }

    function setUploadedFiles() {
        var route = jsRoutes.controllers.Application.getUploadedFiles();
        $.ajax({
            url: route.url,
            type: route.type,
            success: function (res) {
                setUploadedFilesFromArray(res.files, generateUploadedFileBlock, $("#files"), $("#no-files"));
                //alert(JSON.stringify(res.files));
                fileExistsChecker();
            }
        });
    }

    function getSelectedFileSource() {
        var thing = $("#file-source").children(".active");
        return thing.attr("href");
    }

    function getSelectedServerFiles() {
        var filesJSON, files;
        filesJSON = [];
        //filesJSON.fileIDs = [];
        
        files = $("#server-files").find(".selected-server-file").parent().parent().each(function () {
            //filesJSON.fileIDs.push($(this).attr("value"));
            filesJSON.push($(this).attr("value"));
        });

        return filesJSON.toString();
    }

    $(document).ready(function () {
        $("ul.processSelector").each(function () {
            var $active, $content, $links = $(this).parent().find('a');
            $active = $($links.filter('[href="' + location.hash + '"]')[0] || $links[0]);
            $active.parent().addClass('active');

            $content = $($active[0].hash);

            // Hide the remaining content
            $links.not($active).each(function () {
                $(this.hash).hide();
            });

            // Bind the click event handler
            $(this).on('click', 'a', function (e) {
                // Make the old tab inactive.
                $active.parent().removeClass('active');
                $content.hide();

                // Update the variables with the new link and content
                $active = $(this);
                $content = $(this.hash);

                // Make the tab active.
                $active.parent().addClass('active');
                $content.show();

                // Prevent the anchor's default click action
                e.preventDefault();
                highlightSearchText();
            });
        });

        $(".btn-group").each(function () {
            var $active, $content, $links = $(this).find('a');
            $active = $($links.filter('[href="' + location.hash + '"]')[0] || $links[0]);
            $active.addClass('active');

            $content = $($active[0].hash);

            // Hide the remaining content
            $links.not($active).each(function () {
                $(this.hash).hide();
            });

            // Bind the click event handler
            $(this).on('click', 'a', function (e) {
                // Make the old tab inactive.
                $active.removeClass('active');
                $content.hide();

                // Update the variables with the new link and content
                $active = $(this);
                $content = $(this.hash);

                // Make the tab active.
                $active.addClass('active');
                $content.show();

                // Prevent the anchor's default click action
                e.preventDefault();
            });
        });
        setUploadedFiles();
        setServerFiles();

        $('.container').jrumble({
            x: 10,
            y: 10,
            rotation: 0,
            opacity: true,
            opacityMin: 0.5
        });
    });

    $("#file").change(function () {
        validateUploadField($("#file"));
    });

    $(".uploadButton").click(function () {
        var formData, route;

        if (!validateUploadField($("#file"))) {
            return false;
        }

        formData = new FormData(document.getElementById('uploadForm2'));
        route = jsRoutes.controllers.Application.upload();
        $("#uploadProgress").animate({
            height: '40px',
            opacity: 1
        }, {
            duration: 500
        });
        $(".progress-percentage").animate({
            opacity: 1
        }, {
            duration: 500
        });
        $.ajax({
            url: route.url,
            type: route.type,
            //Ajax events
            xhr: function () { // Custom XMLHttpRequest
                var myXhr = $.ajaxSettings.xhr();
                if (myXhr.upload) { // Check if upload property exists
                    myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // For handling the progress of the upload
                }
                return myXhr;
            },
            beforeSend: function (req) {},
            success: function (res) {
                setUploadedFileDetails(res, generateUploadedFileBlock, $("#files"), $("#no-files"));
                $("#uploadProgress").animate({
                    height: '0px',
                    opacity: 0
                }, {
                    duration: 500
                });
                $(".progress-percentage").animate({
                    opacity: 0
                }, {
                    duration: 500
                });
            },
            //error: errorHandler,
            // Form data
            data: formData,
            //Options to tell jQuery not to process data or worry about content-type.
            cache: false,
            contentType: false,
            processData: false
        });
    });

    $('#orderID').on('input propertychange paste', function () {
        $("#process-text-input").removeClass("has-error");
        $("#process-text-input").removeClass("has-success");
        $("#process-text-input").find('span').remove();
    });

    $("#processButton").click(function () {
        var myWindow, formData, route, orderNumber, valid, fileSource;
        formData = new FormData(document.getElementById('processForm'));
        route = jsRoutes.controllers.Application.process();
        orderNumber = $("#orderID").val();

        valid = validateOrderIdField(orderNumber);
        if (!valid) {
            return false;
        }
        //TODO: add logic to check if server/uploaded and set the appropriate get request.
        fileSource = getSelectedFileSource();
        if (fileSource === "#upload-service") {
            $.fileDownload("/process", {
                preparingMessageHtml: "Processing file. The download will begin automatically. Close this dialog when done.",
                failMessageHtml: "The session has expired. Please delete and re-upload files",
                httpMethod: "GET",
                data: {
                    orderID: orderNumber
                }
            });
        } else {
            $.fileDownload("/process-server", {
                preparingMessageHtml: "Processing file. The download will begin automatically. Close this dialog when done.",
                failMessageHtml: "The session has expired. Please delete and re-upload files",
                httpMethod: "GET",
                data: {
                    fileIDs: getSelectedServerFiles(),
                    orderID: orderNumber
                }
            });
        }
    });

    $("#searchButton").click(function () {
        var query, removeDups, javaRegex, appendLine;
        query = $("#searchQuery").val();
        javaRegex = regexToJavaRegex($("#searchQuery").val());
        removeDups = $("#duplicate-checkbox").is(':checked');
        appendLine = $("#new-line-checkbox").is(':checked');
        
        $.fileDownload("/search", {
            preparingMessageHtml: "Processing file. The download will begin automatically. Close this dialog when done.",
            failMessageHtml: "The session has expired or you didn't specify files. Please delete and re-upload files",
            httpMethod: "GET",
            data: {
                query: query,
                removeDuplicates: removeDups,
                appendNewLine: appendLine
            }
        });
    });

    $("#files").on("click", ".deleteFileButton", function (res) {
        deleteFile($(this).parent().parent().parent().attr('value'));
        $(this).parent().parent().animate({
            height: '0px',
            width: '0px',
            opacity: 0
        }, 400, function () {
            $(this).parent().remove();
            fileExistsChecker();
        });
    });


    $("#process-description").readmore({
        maxHeight: 50
    });

    $("#searchQuery").keypress(function () {
        highlightSearchText();
    });

    $("#server-files").on("click", "#serverFileBox", function (res) {
        $(this).find(".tile").toggleClass("selected-server-file");
        if ($(this).find(".tile").hasClass("selected-server-file")) {
            $(this).find(".btn").val("Unselect");
        } else {
            $(this).find(".btn").val("Select");
        }
    });

    $(window).konami({
        cheat: function () {
            $('.container').trigger('startRumble');
        }
    });
});