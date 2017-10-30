$(function () {
    $('.table-button').click(function () {
        var $this = $(this);
        $this.parent().parent().next().toggle();
    })
})