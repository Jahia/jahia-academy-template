<!-- Textarea -->
<div class="form-group"
     ng-class="{'has-error': form[input.name].$invalid&&form[input.name].$dirty}"
     ng-show="resolveLogic()">
    <label class="control-label" for="{{input.name}}">
        {{input.label}}<span ng-if="isRequired()" ng-show="form.$dirty"><sup>&nbsp;<i class="fa fa-asterisk fa-sm"></i></sup></span>
    </label>

    <textarea rows="{{input.rows}}"
              ng-model-options="{allowInvalid:true}"
              ng-model="input.value"
              placeholder="{{input.placeholder}}"
              class="form-control {{input.inputsize}}"
              name="{{input.name}}"
              id="{{input.name}}"
              ng-required="isRequired()"
              ng-readonly="readOnly"
              ff-validations
              ff-logic
              ng-binding="input.value">
        <span class="help-block"
              ng-show="input.helptext != undefined">
            {{input.helptext}}
        </span>
        <span class="help-block"
              ng-repeat="(validationName, validation) in input.validations"
              ng-show="form[input.name].$error[(validationName | normalize)]&&form[input.name].$dirty">
                {{validation.message}}
        </span>
</div>
