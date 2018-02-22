<!-- Multiple Radios (inline) -->
<div class="form-group"
     ng-class="{'has-error': form[input.name].$invalid&&form[input.name].$dirty}"
     ng-show="resolveLogic()">
    <label class="control-label">
        {{input.label}}
        <span ng-if="isRequired()"
              ng-show="asteriskResolver()">
            <sup>&nbsp;<i class="fa fa-asterisk fa-sm"></i></sup>
        </span>
    </label>


    <div class="radio">
        <label class="radio-inline" ng-repeat="(radiok, radiov) in input.radios | filter: 'true' : null : visible">
            <input type="radio"
                   name="{{input.name}}"
                   ng-model="input.value"
                   ng-model-options="{'allowInvalid':true}"
                   value="{{radiov.key}}"
                   ng-required="isRequired()"
                   ng-disabled="readOnly"
                   ng-change="makeDirty()"
                   ff-validations
                   ff-logic
                   ff-focus-tracker="{{input.name}}_{{radiov.key}}">
            {{radiov.value}}
        </label>
        <span class="help-block"
              ng-show="input.helptext != undefined">
            {{input.helptext}}
        </span>
        <span class="help-block"
              ng-repeat="(validationName, validation) in input.validations"
              ng-show="showErrorMessage(validationName)">
                {{validation.message}}
        </span>
    </div>
</div>